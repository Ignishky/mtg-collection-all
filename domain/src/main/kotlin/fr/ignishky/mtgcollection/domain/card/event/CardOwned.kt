package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import java.time.Instant.now

class CardOwned(
    aggregateId: CardId,
    isOwnedFoil: Boolean,
) : Event<CardId, Card, CardOwnedPayload>(
    0,
    aggregateId,
    Card::class,
    CardOwnedPayload(
        isOwnedFoil,
    ),
    now(),
)

data class CardOwnedPayload(
    val ownedFoil: Boolean =false,
) : Payload
