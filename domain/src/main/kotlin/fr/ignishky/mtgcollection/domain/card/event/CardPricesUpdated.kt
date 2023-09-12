package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Instant
import kotlin.reflect.KClass

class CardPricesUpdated(aggregateId: CardId, prices: CardPrices) :
    Event<CardId, Card, CardPricesUpdated.PricesUpdatedPayload>(
        0,
        aggregateId,
        Card::class,
        PricesUpdatedPayload(prices),
        Instant.now(),
    ) {

    override fun apply(aggregate: Aggregate<*>): Card {
        return (aggregate as Card).copy(prices = payload.prices)
    }

    data class PricesUpdatedPayload(
        val prices: CardPrices,
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
