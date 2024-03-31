package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardProperty
import java.time.Instant.now

class CardUpdated(
    aggregateId: CardId,
    vararg properties: CardProperty,
) : Event<CardId, Card, CardUpdatedPayload>(
    0,
    aggregateId,
    Card::class,
    CardUpdatedPayload.from(*properties),
    now(),
)

data class CardUpdatedPayload(
    val properties: Map<String, String>,
) : Payload {
    @Suppress("unused")
    constructor() : this(mapOf())

    companion object {
        fun from(vararg properties: CardProperty) = CardUpdatedPayload(
            properties.associateBy({ property -> property.propertyName().name }) { property -> property.propertyValue() }
        )
    }

}
