package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.cqrs.event.spi.postgres.EventRepository
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardEventStorePort
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named

@Named
class CardEventPostgresStore(
    private val eventRepository: EventRepository,
    private val jsonMapper: ObjectMapper
) : CardEventStorePort {

    override fun get(setCode: SetCode): List<Card> {
        // TODO: find a way to only find card events from a given set
        return eventRepository.findByAggregateNameOrderByInstantAsc(Card::class.java.simpleName)
            .groupBy(EventEntity::aggregateId)
            .map {
                it.value
                    .map {
                        val payloadClass = Class.forName("fr.ignishky.mtgcollection.domain.card.event.${it.label}\$${it.label}Payload")
                        val payload = jsonMapper.readValue(it.payload, payloadClass) as Payload
                        payload.asEvent(it.aggregateId)
                    }
                    .fold(Card()) { aggregate, event -> event.apply(aggregate) as Card }
            }
    }

}
