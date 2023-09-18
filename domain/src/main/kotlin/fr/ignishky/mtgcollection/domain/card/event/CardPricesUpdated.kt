package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.model.Price
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Instant.now

class CardPricesUpdated(
    correlationId: CorrelationId,
    aggregateId: CardId,
    prices: CardPrices,
) :
    Event<CardId, Card, CardPricesUpdated.CardPricesUpdatedPayload>(
        0,
        aggregateId,
        Card::class,
        CardPricesUpdatedPayload(prices.scryfall.eur, prices.scryfall.eurFoil, prices.scryfall.usd, prices.scryfall.usdFoil),
        now(),
        correlationId,
    ) {

    override fun apply(aggregate: Aggregate<*>) = (aggregate as Card).copy(
        prices = CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil))
    )

    data class CardPricesUpdatedPayload(
        val scryfallEur: Long,
        val scryfallEurFoil: Long,
        val scryfallUsd: Long,
        val scryfallUsdFoil: Long,
    ) : Payload {
        constructor() : this(0, 0, 0, 0)
    }

    @Named
    class CardPricesUpdatedHandler(private val cardStore: CardStorePort) : EventHandler<CardPricesUpdated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: Event<*, *, *>) {
            val cardPricesUpdated = event as CardPricesUpdated
            logger.info { "Updating card prices '${cardPricesUpdated.aggregateId.value}'..." }
            cardStore.update(
                cardPricesUpdated.aggregateId,
                CardPrices(
                    Price(
                        cardPricesUpdated.payload.scryfallEur,
                        cardPricesUpdated.payload.scryfallEurFoil,
                        cardPricesUpdated.payload.scryfallUsd,
                        cardPricesUpdated.payload.scryfallUsdFoil
                    )
                )
            )
        }

        override fun listenTo() = CardPricesUpdated::class

    }

}
