package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedPayload
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.*
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Instant.now
import kotlin.reflect.KClass

class CardUpdated(aggregateId: CardId, vararg properties: CardProperty) :
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
            CardName(payload.properties.getOrElse(NAME.name) { aggregate.name.value } as String),
            CardSetCode(payload.properties.getOrElse(SET_CODE.name) { aggregate.setCode.value } as String),
            CardImages(payload.properties.getOrElse(IMAGES.name) { aggregate.images.value } as List<CardImage>),
            CardNumber(payload.properties.getOrElse(COLLECTION_NUMBER.name) { aggregate.collectionNumber.value } as String),
            aggregate.prices,
        )
    }

    data class CardUpdatedPayload(
        val properties: Map<String, Any>,
    ) : Payload {

        companion object {
            fun from(vararg properties: CardProperty): CardUpdatedPayload {
                return CardUpdatedPayload(
                    properties.associateBy({ property -> property.propertyName().name }) { property -> property.propertyValue() }
                )
            }
        }

        override fun asEvent(aggregateId: String): Event<*, *, *> {
            TODO("Not yet implemented")
        }

    }

    @Named
    class CardUpdatedHandler(private val cardStore: CardStorePort) : EventHandler<CardUpdated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: Event<*, *, *>) {
            val cardUpdated = event as CardUpdated
            val existingCard = cardStore.get(cardUpdated.aggregateId)
            logger.info { "Updating card '${existingCard.name.value}'..." }
            cardStore.store(cardUpdated.apply(existingCard))
        }

        override fun listenTo(): KClass<CardUpdated> {
            return CardUpdated::class
        }

    }

}
