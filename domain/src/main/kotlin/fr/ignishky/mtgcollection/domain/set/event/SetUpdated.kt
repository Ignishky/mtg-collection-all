package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedPayload
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.*
import java.time.Instant.now
import java.time.LocalDate

class SetUpdated(
    aggregateId: SetId,
    vararg properties: SetProperty,
) :
    Event<SetId, Set, SetUpdatedPayload>(
        0,
        aggregateId,
        Set::class,
        SetUpdatedPayload.from(*properties),
        now(),
    ) {

    override fun apply(aggregate: Aggregate<*>): Set {
        aggregate as Set
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
        @Suppress("unused")
        constructor() : this(HashMap())

        companion object {
            fun from(vararg properties: SetProperty) = SetUpdatedPayload(
                properties.associateBy({ property -> property.propertyName().name }) { property -> property.propertyValue() }
            )
        }

        fun toProperties() = properties.map { SetProperty.PropertyName.valueOf(it.key).withValue(it.value) }

    }

}
