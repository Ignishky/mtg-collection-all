package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate

@Named
class CardPostgresStore(
    private val jdbcTemplate: JdbcTemplate,
) : CardStorePort {

    override fun add(card: Card) {
        val scryfall = card.prices.scryfall
        jdbcTemplate.update(
            "INSERT INTO cards (id, name, set_code, images, collection_number, scryfall_prices) VALUES (?, ?, ?, ?, ?, ?)",
            card.id.value,
            card.name.value,
            card.setCode.value,
            card.images.value.joinToString { it.value },
            card.collectionNumber.value,
            "${scryfall.eur}|${scryfall.eurFoil}|${scryfall.usd}|${scryfall.usdFoil}",
        )
    }

    override fun update(card: Card) {
        val scryfall = card.prices.scryfall
        jdbcTemplate.update(
            "UPDATE cards SET name=?, set_code=?, images=?, collection_number=?, scryfall_prices=? WHERE id=?",
            card.name.value,
            card.setCode.value,
            card.images.value.joinToString { it.value },
            card.collectionNumber.value,
            "${scryfall.eur}|${scryfall.eurFoil}|${scryfall.usd}|${scryfall.usdFoil}",
            card.id.value
        )
    }

    override fun get(id: CardId) = jdbcTemplate.queryForObject("SELECT * FROM cards WHERE id=?", CardRowMapper(), id.value)!!

    override fun getAll(code: SetCode) = jdbcTemplate.query("SELECT * FROM cards WHERE set_code=?", CardRowMapper(), code.value)

}
