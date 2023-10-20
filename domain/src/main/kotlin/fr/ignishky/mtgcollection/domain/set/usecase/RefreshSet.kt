package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetEventStorePort
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import jakarta.inject.Named
import mu.KotlinLogging.logger

class RefreshSet : Command {

    @Named
    class RefreshSetHandler(
        private val setReferer: SetRefererPort,
        private val setEventStorePort: SetEventStorePort,
    ) : CommandHandler<RefreshSet> {

        private val logger = logger {}

        override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {

            val knownSetsById = setEventStorePort.getAll().associateBy { it.id }
            logger.info { "Refreshing ${knownSetsById.size} sets..." }

            return setReferer.getAllSets()
                .mapNotNull {
                    if (knownSetsById[it.id] == null) {
                        SetCreated(correlationId, it.id, it.code, it.name, it.type, it.icon, it.releasedAt)
                    } else {
                        setUpdated(knownSetsById[it.id]!!, it, correlationId)
                    }
                }
        }

        private fun setUpdated(knownSet: Set, newSet: Set, correlationId: CorrelationId): SetUpdated? {
            val delta = knownSet.updatedFields(newSet)
            return if (delta.isNotEmpty()) {
                SetUpdated(correlationId, newSet.id, *delta.toTypedArray())
            } else {
                null
            }
        }

        override fun listenTo() = RefreshSet::class

    }

}
