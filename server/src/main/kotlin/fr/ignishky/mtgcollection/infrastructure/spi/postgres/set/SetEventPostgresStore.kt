package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.cqrs.event.spi.postgres.EventRepository
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetEventStorePort
import jakarta.inject.Named

@Named
class SetEventPostgresStore(
    private val eventRepository: EventRepository,
    private val jsonMapper: ObjectMapper
) : SetEventStorePort {

    override fun getAll(): List<Set> {
        return eventRepository.findByAggregateNameOrderByInstantAsc(Set::class.java.simpleName)
            .groupBy(EventEntity::aggregateId)
            .map {
                it.value
                    .map {
                        val payloadClass = Class.forName("fr.ignishky.mtgcollection.domain.set.event.${it.label}\$${it.label}Payload")
                        val payload = jsonMapper.readValue(it.payload, payloadClass) as Payload
                        payload.asEvent(it.aggregateId)
                    }
                    .fold(Set()) { aggregate, event -> event.apply(aggregate) as Set }
            }
    }

}
