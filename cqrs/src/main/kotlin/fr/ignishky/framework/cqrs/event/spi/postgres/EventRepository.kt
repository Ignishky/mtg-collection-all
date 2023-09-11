package fr.ignishky.framework.cqrs.event.spi.postgres

import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface EventRepository : JpaRepository<EventEntity, Int> {

    fun findByAggregateNameOrderByInstantAsc(aggregateName: String): List<EventEntity>

}
