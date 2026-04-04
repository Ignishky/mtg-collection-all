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
                "INSERT INTO sets (id, code, name, type, icon, released_at, nb_cards, nb_owned_cards) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                it.id.value,
                it.code.value,
                it.name.value,
                it.type.value,
                it.icon.value,
                it.releasedAt.value,
                it.nbCards.value,
                it.nbOwnedCards.value,
            )
        }
    }

    fun saveCards(vararg cards: Card) {
        cards.forEach { card ->
            template.update(
                "INSERT INTO cards (id, name, set_code, images, collection_number, scryfall_prices, finishes, nb_owned_non_foil, nb_owned_foil, colors) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                card.id.value,
                card.name.value,
                card.setCode.value,
                card.images.value.joinToString { (value) -> value },
                card.collectionNumber.value,
                "${card.prices.scryfall.eur}|${card.prices.scryfall.eurFoil}|${card.prices.scryfall.usd}|${card.prices.scryfall.usdFoil}",
                card.finishes.value.joinToString { (value) -> value },
                card.nbOwnedNonFoil.value,
                card.nbOwnedFoil.value,
                card.colors.value.joinToString { color -> color.name },
            )
        }
    }

    fun getEvents(): List<Event<*, *, *>> = template.query("SELECT * FROM events", EventRowMapper(objectMapper))

    fun getSets(): List<Set> = template.query("SELECT * FROM sets", SetRowMapper())

    fun getCards(): List<Card> = template.query("SELECT * FROM cards", CardRowMapper())

}
