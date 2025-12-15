package fr.ignishky.mtgcollection.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.CardRowMapper
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.event.EventRowMapper
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.SetRowMapper
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Date

@Named
class JdbcUtils(
    private val objectMapper: ObjectMapper,
    private val template: JdbcTemplate,
) {

    fun dropAll() {
        template.execute("TRUNCATE TABLE events")
        template.execute("TRUNCATE TABLE sets")
        template.execute("TRUNCATE TABLE cards")
    }

    fun saveEvents(vararg events: Event<*, *, *>) {
        events.forEach {
            template.update(
                "INSERT INTO events (aggregate_id, aggregate_name, label, instant, payload) VALUES (?, ?, ?, ?, ?::jsonb)",
                it.aggregateId.value(),
                it.aggregateClass.simpleName,
                it::class.simpleName,
                Date(it.instant.toEpochMilli()),
                objectMapper.writeValueAsString(it.payload),
            )
        }
    }

    fun saveSets(vararg sets: Set) {
        sets.forEach {
            template.update(
                "INSERT INTO sets (id, code, name, type, icon, released_at) VALUES (?, ?, ?, ?, ?, ?)",
                it.id.value,
                it.code.value,
                it.name.value,
                it.type.value,
                it.icon.value,
                it.releasedAt.value,
            )
        }
    }

    fun saveCards(vararg cards: Card) {
        cards.forEach {
            template.update(
                "INSERT INTO cards (id, name, set_code, images, collection_number, scryfall_prices, finishes, nb_owned_non_foil, nb_owned_foil, colors) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                it.id.value,
                it.name.value,
                it.setCode.value,
                it.images.value.joinToString { (value) -> value },
                it.collectionNumber.value,
                "${it.prices.scryfall.eur}|${it.prices.scryfall.eurFoil}|${it.prices.scryfall.usd}|${it.prices.scryfall.usdFoil}",
                it.finishes.value.joinToString { (value) -> value },
                it.nbOwnedNonFoil.value,
                it.nbOwnedFoil.value,
                it.colors.value.joinToString { it.name },
            )
        }
    }

    fun getEvents(): List<Event<*, *, *>> = template.query("SELECT * FROM events", EventRowMapper(objectMapper))

    fun getSets(): List<Set> = template.query("SELECT * FROM sets", SetRowMapper())

    fun getCards(): List<Card> = template.query("SELECT * FROM cards", CardRowMapper())

}
