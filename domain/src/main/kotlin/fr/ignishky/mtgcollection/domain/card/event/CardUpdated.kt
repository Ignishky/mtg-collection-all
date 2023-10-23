package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedPayload
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Instant.now

class CardUpdated(
    aggregateId: CardId,
    vararg properties: CardProperty,
) :
    Event<CardId, Card, CardUpdatedPayload>(
        0,
        aggregateId,
        Card::class,
        CardUpdatedPayload.from(*properties),
        now(),
    ) {

    override fun apply(aggregate: Aggregate<*>): Card {
        aggregate as Card
        return Card(
            aggregateId,
            CardName(payload.properties.getOrElse(NAME.name) { aggregate.name.value }),
            CardSetCode(payload.properties.getOrElse(SET_CODE.name) { aggregate.setCode.value }),
            getCardImages(aggregate),
            CardNumber(payload.properties.getOrElse(COLLECTION_NUMBER.name) { aggregate.collectionNumber.value }),
            aggregate.prices,
            getCardFinishes(aggregate),
            aggregate.isOwned,
            aggregate.isOwnedFoil,
        )
    }

    private fun getCardImages(aggregate: Card) = if (payload.properties.containsKey(IMAGES.name)) {
        CardImages(payload.properties[IMAGES.name]!!.split(", ").map { CardImage(it) })
    } else {
        aggregate.images
    }

    private fun getCardFinishes(aggregate: Card) = if (payload.properties.containsKey(FINISHES.name)) {
        CardFinishes(payload.properties[FINISHES.name]!!.split(", ").map { CardFinish(it) })
    } else {
        aggregate.finishes
    }

    data class CardUpdatedPayload(
        val properties: Map<String, String>,
    ) : Payload {
        @Suppress("unused")
        constructor() : this(mapOf())

        companion object {
            fun from(vararg properties: CardProperty) = CardUpdatedPayload(
                properties.associateBy({ property -> property.propertyName().name }) { property -> property.propertyValue() }
            )
        }

        fun toProperties() = properties.map { CardProperty.PropertyName.valueOf(it.key).withValue(it.value) }

    }

    @Named
    class CardUpdatedHandler(
        private val cardProjectionPort: CardProjectionPort,
    ) : EventHandler<CardUpdated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: Event<*, *, *>) {
            val cardUpdated = event as CardUpdated
            logger.info { "Updating card '${cardUpdated.aggregateId.value}'..." }
            cardProjectionPort.update(cardUpdated.aggregateId, cardUpdated.payload.toProperties())
        }

        override fun listenTo() = CardUpdated::class

    }

}
