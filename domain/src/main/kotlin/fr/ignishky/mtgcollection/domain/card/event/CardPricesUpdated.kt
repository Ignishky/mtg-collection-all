package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import java.time.Instant.now

class CardPricesUpdated(
    aggregateId: CardId,
    prices: CardPrices,
) : Event<CardId, Card, CardPricesUpdatedPayload>(
    0,
    aggregateId,
    Card::class,
    CardPricesUpdatedPayload(prices.scryfall.eur, prices.scryfall.eurFoil, prices.scryfall.usd, prices.scryfall.usdFoil),
    now(),
)

data class CardPricesUpdatedPayload(
    val scryfallEur: Long,
    val scryfallEurFoil: Long,
    val scryfallUsd: Long,
    val scryfallUsdFoil: Long,
) : Payload {
    @Suppress("unused")
    constructor() : this(0, 0, 0, 0)
}
