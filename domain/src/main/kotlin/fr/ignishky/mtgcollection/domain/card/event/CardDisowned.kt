package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import java.time.Instant

class CardDisowned(
    aggregateId: CardId,
) : Event<CardId, Card, CardDisownedPayload>(
    0,
    aggregateId,
    Card::class,
    CardDisownedPayload(),
    Instant.now(),
)

class CardDisownedPayload : Payload
