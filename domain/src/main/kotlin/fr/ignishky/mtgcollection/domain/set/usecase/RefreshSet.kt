package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetProperty
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.LocalDate.now

class RefreshSet : Command

@Named
class RefreshSetHandler(
    private val setReferer: SetRefererPort,
    private val setProjectionPort: SetProjectionPort,
) : CommandHandler<RefreshSet> {

    private val logger = KotlinLogging.logger {}

    override fun handle(command: Command): List<Event<*, *, *>> {

        val knownSetsById = setProjectionPort.getAll().associateBy { it.id }
        logger.info { "Refreshing ${knownSetsById.size} sets..." }

        return setReferer.getAllSets()
            .filter { set -> onlyReleasedSet(set) }
            .mapNotNull { set ->
                if (knownSetsById[set.id] == null) {
                    createSet(set)
                } else {
                    updateSet(knownSetsById[set.id]!!, set)
                }
            }
    }

    private fun onlyReleasedSet(it: Set) = it.releasedAt.value.isBefore(now().plusDays(1))

    private fun createSet(set: Set): SetCreated {
        setProjectionPort.add(set)
        return SetCreated(set)
    }

    private fun updateSet(knownSet: Set, newSet: Set): SetUpdated? {
        val delta = knownSet.updatedFields(newSet)
        return if (delta.isNotEmpty()) {
            setProjectionPort.update(knownSet.id, delta)
            SetUpdated(newSet.id, *delta.toTypedArray<SetProperty>())
        } else {
            null
        }
    }

    override fun listenTo() = RefreshSet::class

}
