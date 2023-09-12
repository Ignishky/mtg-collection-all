package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedPayload
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.*
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import mu.KotlinLogging.logger
import java.time.Instant.now
import java.time.LocalDate
import kotlin.reflect.KClass

class SetUpdated(aggregateId: SetId, vararg properties: SetProperty) :
    Event<SetId, Set, SetUpdatedPayload>(
        0,
        aggregateId,
        Set::class,
        SetUpdatedPayload.from(*properties),
        now(),
    ) {

    override fun apply(aggregate: Set): Set {
        return Set(
            aggregate.id,
            SetCode(payload.properties.getOrElse(CODE.name) { aggregate.code.value }),
            SetName(payload.properties.getOrElse(NAME.name) { aggregate.name.value }),
            SetType(payload.properties.getOrElse(TYPE.name) { aggregate.type.value }),
            SetIcon(payload.properties.getOrElse(ICON.name) { aggregate.icon.value }),
            SetReleasedAt(LocalDate.parse(payload.properties.getOrElse(RELEASED_AT.name) { aggregate.releasedAt.value.toString() })),
        )
    }

    data class SetUpdatedPayload(
        val properties: Map<String, String>,
    ) : Payload {

        companion object {
            fun from(vararg properties: SetProperty): SetUpdatedPayload {
                return SetUpdatedPayload(
                    properties.associateBy({ property -> property.propertyName().name }) { property -> property.propertyValue() }
                )
            }
        }

    }

    @Named
    class SetUpdatedHandler(private val setStore: SetStorePort) : EventHandler<SetUpdated> {

        private val logger = logger {}

        override fun handle(event: Event<*, *, *>) {
            val setUpdated = event as SetUpdated
            val existingSet = setStore.get(setUpdated.aggregateId)
            logger.info { "Updating set '${existingSet.name.value}'..." }
            setStore.store(setUpdated.apply(existingSet))
        }

        override fun listenTo(): KClass<SetUpdated> {
            return SetUpdated::class
        }

    }

}
