package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.Set
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
            .filter { it.releasedAt.value.isBefore(now().plusDays(1)) }
            .mapNotNull {
                if (knownSetsById[it.id] == null) {
                    setCreated(it)
                } else {
                    setUpdated(knownSetsById[it.id]!!, it)
                }
            }
    }

    private fun setCreated(it: Set): SetCreated {
        val event = SetCreated(it.id, it.code, it.name, it.type, it.icon, it.releasedAt)
        setProjectionPort.add(event.apply(Set()))
        return event
    }

    private fun setUpdated(knownSet: Set, newSet: Set): SetUpdated? {
        val delta = knownSet.updatedFields(newSet)
        return if (delta.isNotEmpty()) {
            val event = SetUpdated(newSet.id, *delta.toTypedArray())
            setProjectionPort.update(knownSet.id, delta)
            event
        } else {
            null
        }
    }

    override fun listenTo() = RefreshSet::class

}
