package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated.CardCreatedPayload
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Instant.now

class CardCreated(
    correlationId: CorrelationId,
    aggregateId: CardId,
    name: CardName,
    setCode: CardSetCode,
    prices: CardPrices,
    images: CardImages,
    collectionNumber: CardNumber,
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
        ),
        now(),
        correlationId,
    ) {

    override fun apply(aggregate: Aggregate<*>) = Card(
        aggregateId,
        CardName(payload.name),
        CardSetCode(payload.setCode),
        CardImages(payload.images.map { CardImage(it) }),
        CardNumber(payload.collectionNumber),
        CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil)),
    )

    data class CardCreatedPayload(
        val name: String,
        val setCode: String,
        val scryfallEur: Long,
        val scryfallEurFoil: Long,
        val scryfallUsd: Long,
        val scryfallUsdFoil: Long,
        val images: List<String>,
        val collectionNumber: String,
    ) : Payload {
        constructor() : this("", "", 0, 0, 0, 0, emptyList(), "")
    }

    @Named
    class CardCreatedHandler(
        private val cardProjectionPort: CardProjectionPort,
    ) : EventHandler<CardCreated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: Event<*, *, *>) {
            val cardCreated = event as CardCreated
            logger.info { "Creating card '${cardCreated.payload.name}'..." }
            cardProjectionPort.add(cardCreated.apply(Card()))
        }

        override fun listenTo() = CardCreated::class

    }

}
