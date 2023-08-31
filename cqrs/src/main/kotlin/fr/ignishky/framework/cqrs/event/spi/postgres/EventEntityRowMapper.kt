package fr.ignishky.framework.cqrs.event.spi.postgres

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class EventEntityRowMapper : RowMapper<EventEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): EventEntity {
        return EventEntity(
            rs.getLong("id"),
            rs.getString("aggregate_id"),
            rs.getString("aggregate_name"),
            rs.getString("label"),
            rs.getTimestamp("instant").toInstant(),
            rs.getString("payload"),
            rs.getString("correlation_id")
        )
    }
}
