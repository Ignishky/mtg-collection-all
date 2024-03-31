package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetProperty
import java.time.Instant.now

class SetUpdated(
    aggregateId: SetId,
    vararg properties: SetProperty,
) : Event<SetId, Set, SetUpdatedPayload>(
    0,
    aggregateId,
    Set::class,
    SetUpdatedPayload.from(*properties),
    now(),
)

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
