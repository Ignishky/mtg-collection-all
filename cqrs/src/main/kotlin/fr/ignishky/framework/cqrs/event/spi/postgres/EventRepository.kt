package fr.ignishky.framework.cqrs.event.spi.postgres

import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

@Named
interface EventRepository : JpaRepository<EventEntity, Int> {

    fun findByAggregateNameOrderByInstantAsc(aggregateName: String): List<EventEntity>

    @Query("SELECT e FROM EventEntity e WHERE e.label IN ('CardCreated', 'CardUpdated') AND e.payload.setCode = :setCode")
    fun findCardEventsBySetCode(setCode: String): List<EventEntity>

}
