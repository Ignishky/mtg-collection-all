package fr.ignishky.mtgcollection.infrastructure.spi.postgres.event

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardEventStorePort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Named
class CardEventPostgresStore(
    private val objectMapper: ObjectMapper,
    private val jdbcTemplate: JdbcTemplate,
) : CardEventStorePort {

    override fun getAll(code: SetCode): List<Card> {
        val cardIds = getCardIds(code)

        if (cardIds.isEmpty()) return emptyList()

        return getCardEvents(cardIds)
            .groupBy { it.aggregateId.value() }
            .map {
                it.value.fold(Card()) { aggregate, event -> event.apply(aggregate) as Card }
            }
    }

    private fun getCardIds(code: SetCode) =
        jdbcTemplate.queryForList("SELECT aggregate_id FROM events WHERE label=? AND payload->>'setCode'=?", "CardCreated", code.value)
            .map { it["aggregate_id"] as String }

    private fun getCardEvents(cardIds: List<String>): MutableList<Event<*, *, *>> =
        NamedParameterJdbcTemplate(jdbcTemplate.dataSource!!)
            .query(
                "SELECT * FROM events WHERE aggregate_id IN (:ids) AND label IN ('CardCreated', 'CardUpdated')",
                MapSqlParameterSource("ids", cardIds),
                EventRowMapper(objectMapper)
            )

}
