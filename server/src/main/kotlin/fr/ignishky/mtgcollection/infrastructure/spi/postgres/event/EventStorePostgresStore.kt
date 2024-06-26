package fr.ignishky.mtgcollection.infrastructure.spi.postgres.event

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventRepository
import fr.ignishky.framework.domain.AggregateId
import jakarta.inject.Named
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Date
import java.sql.PreparedStatement

@Named
class EventStorePostgresStore(
    private val objectMapper: ObjectMapper,
    private val jdbcTemplate: JdbcTemplate,
) : EventRepository {

    override fun saveAll(events: List<Event<*, *, *>>) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO events (aggregate_id, aggregate_name, label, instant, payload) VALUES (?, ?, ?, ?, ?::jsonb)",
            EventBatchPreparedStatementSetter(events, objectMapper)
        )
    }

    override fun getAll(id: AggregateId): List<Event<*, *, *>> {
        return emptyList()
    }

    private class EventBatchPreparedStatementSetter(
        private val events: List<Event<*, *, *>>,
        private val objectMapper: ObjectMapper,
    ) : BatchPreparedStatementSetter {

        override fun setValues(ps: PreparedStatement, i: Int) {
            val event = events[i]
            ps.setString(1, event.aggregateId.value())
            ps.setString(2, event.aggregateClass.simpleName)
            ps.setString(3, event::class.simpleName)
            ps.setDate(4, Date(event.instant.toEpochMilli()))
            ps.setString(5, objectMapper.writeValueAsString(event.payload))
        }

        override fun getBatchSize() = events.size
    }

}
