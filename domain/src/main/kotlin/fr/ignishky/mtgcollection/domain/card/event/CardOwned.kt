package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import java.time.Instant.now

class CardOwned(
    aggregateId: CardId,
    isOwnedFoil: CardIsOwnedFoil,
) : Event<CardId, Card, CardOwnedPayload>(
    0,
    aggregateId,
    Card::class,
    CardOwnedPayload(
        isOwnedFoil.value,
    ),
    now(),
)

data class CardOwnedPayload(
    val ownedFoil: Boolean,
) : Payload {
    @Suppress("unused")
    constructor() : this(false)
}
