package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.event.CardCreated.CardCreatedPayload
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Instant.now
import kotlin.reflect.KClass

class CardCreated(
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
    ) {

    override fun apply(aggregate: Card): Card {
        return Card(
            aggregateId,
            CardName(payload.name),
            CardSetCode(payload.setCode),
            CardImages(payload.images.map { CardImage(it) }),
            CardNumber(payload.collectionNumber),
            CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil)),
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
    ) : Payload

    @Named
    class CardCreatedHandler(
        private val cardStore: CardStorePort
    ) : EventHandler<CardCreated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: Event<*, *, *>) {
            val cardCreated = event as CardCreated
            logger.info { "Creating card '${cardCreated.payload.name}'..." }
            cardStore.store(cardCreated.apply(Card()))
        }

        override fun listenTo(): KClass<CardCreated> {
            return CardCreated::class
        }

    }

}
