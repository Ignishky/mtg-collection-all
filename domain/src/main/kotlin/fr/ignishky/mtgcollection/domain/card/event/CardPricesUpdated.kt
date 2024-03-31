package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.model.Price
import java.time.Instant.now

class CardPricesUpdated(
    aggregateId: CardId,
    prices: CardPrices,
) :
    Event<CardId, Card, CardPricesUpdatedPayload>(
        0,
        aggregateId,
        Card::class,
        CardPricesUpdatedPayload(prices.scryfall.eur, prices.scryfall.eurFoil, prices.scryfall.usd, prices.scryfall.usdFoil),
        now(),
    ) {

    override fun apply(aggregate: Aggregate<*>) = (aggregate as Card).copy(
        prices = CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil))
    )

}

data class CardPricesUpdatedPayload(
    val scryfallEur: Long,
    val scryfallEurFoil: Long,
    val scryfallUsd: Long,
    val scryfallUsdFoil: Long,
) : Payload {
    @Suppress("unused")
    constructor() : this(0, 0, 0, 0)
}
