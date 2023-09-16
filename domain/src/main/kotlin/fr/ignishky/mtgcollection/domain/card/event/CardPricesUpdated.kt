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
import kotlin.reflect.KClass

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

    override fun apply(aggregate: Aggregate<*>): Card {
        return (aggregate as Card).copy(
            prices = CardPrices(
                Price(
                    payload.scryfallEur,
                    payload.scryfallEurFoil,
                    payload.scryfallUsd,
                    payload.scryfallUsdFoil
                )
            )
        )
    }

    data class CardPricesUpdatedPayload(
        val scryfallEur: Long,
        val scryfallEurFoil: Long,
        val scryfallUsd: Long,
        val scryfallUsdFoil: Long,
    ) : Payload

    @Named
    class CardPricesUpdatedHandler(private val cardStore: CardStorePort) : EventHandler<CardPricesUpdated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: Event<*, *, *>) {
            val cardPricesUpdated = event as CardPricesUpdated
            val existingCard = cardStore.get(cardPricesUpdated.aggregateId)
            logger.info { "Updating card prices '${existingCard.name.value}'..." }
            cardStore.store(cardPricesUpdated.apply(existingCard))
        }

        override fun listenTo(): KClass<CardPricesUpdated> {
            return CardPricesUpdated::class
        }

    }

}
