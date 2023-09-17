package fr.ignishky.mtgcollection.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.mapper.CardEntityRowMapper
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.mapper.SetEntityRowMapper
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate

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

    fun save(sets: List<Set>, cards: List<Card>) {
        sets.forEach {
            template.update(
                "INSERT INTO sets VALUES (?, ?, ?, ?, ?, ?)",
                it.id.value,
                it.code.value,
                it.name.value,
                it.type.value,
                it.icon.value,
                it.releasedAt.value,
            )
        }
        cards.forEach {
            template.update(
                "INSERT INTO cards VALUES (?, ?, ?, ?, ?, ?)",
                it.id.value,
                it.name.value,
                it.setCode.value,
                it.images.value.joinToString { it.value },
                it.collectionNumber.value,
                "${it.prices.scryfall.eur}|${it.prices.scryfall.eurFoil}|${it.prices.scryfall.usd}|${it.prices.scryfall.usdFoil}",
            )
        }
    }

    fun getEvents() = template.query("SELECT * FROM events", EventRowMapper(objectMapper))

    fun getSets() = template.query("SELECT * FROM sets", SetEntityRowMapper())

    fun getCards() = template.query("SELECT * FROM cards", CardEntityRowMapper())

}
