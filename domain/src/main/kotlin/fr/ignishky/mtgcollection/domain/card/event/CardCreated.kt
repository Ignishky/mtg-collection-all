package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.card.model.*
import java.time.Instant.now

class CardCreated(
    aggregateId: CardId,
    name: CardName,
    setCode: CardSetCode,
    prices: CardPrices,
    images: CardImages,
    collectionNumber: CardNumber,
    finishes: CardFinishes,
) :
    Event<CardId, Card, CardCreatedPayload>(
        0,
        aggregateId,
        Card::class,
        CardCreatedPayload(
            name.value,
            setCode.value,
            prices.scryfall.eur,
            prices.scryfall.eurFoil,
            prices.scryfall.usd,
            prices.scryfall.usdFoil,
            images.value.map { it.value },
            collectionNumber.value,
            finishes.value.map { it.value }
        ),
        now(),
    ) {

    constructor(card: Card) : this(card.id, card.name, card.setCode, card.prices, card.images, card.collectionNumber, card.finishes)

    override fun apply(aggregate: Aggregate<*>) = Card(
        aggregateId,
        CardName(payload.name),
        CardSetCode(payload.setCode),
        CardImages(payload.images.map { CardImage(it) }),
        CardNumber(payload.collectionNumber),
        CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil)),
        CardFinishes(payload.finishes.map { CardFinish(it) }),
    )

}

data class CardCreatedPayload(
    val name: String,
    val setCode: String,
    val scryfallEur: Long,
    val scryfallEurFoil: Long,
    val scryfallUsd: Long,
    val scryfallUsdFoil: Long,
    val images: List<String>,
    val collectionNumber: String,
    val finishes: List<String>,
) : Payload {
    @Suppress("unused")
    constructor() : this("", "", 0, 0, 0, 0, emptyList(), "", emptyList())
}
