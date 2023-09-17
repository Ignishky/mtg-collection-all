package fr.ignishky.mtgcollection.infrastructure.spi.postgres.event

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetEventStorePort
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate

@Named
class SetEventPostgresStore(
    private val objectMapper: ObjectMapper,
    private val jdbcTemplate: JdbcTemplate,
) : SetEventStorePort {

    override fun getAll() = jdbcTemplate
        .query("SELECT * FROM events WHERE aggregate_name=?", EventRowMapper(objectMapper), "Set")
        .groupBy { it.aggregateId.value() }
        .map {
            it.value.fold(Set()) { aggregate, event -> event.apply(aggregate) as Set }
        }

}
