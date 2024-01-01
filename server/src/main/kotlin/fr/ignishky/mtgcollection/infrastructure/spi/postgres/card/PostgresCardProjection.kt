package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate

@Named
class PostgresCardProjection(
    private val jdbcTemplate: JdbcTemplate,
) : CardProjectionPort {

    override fun add(card: Card) {
        val scryfall = card.prices.scryfall
        jdbcTemplate.update(
            "INSERT INTO cards (id, name, set_code, images, collection_number, scryfall_prices, finishes) VALUES (?, ?, ?, ?, ?, ?, ?)",
            card.id.value,
            card.name.value,
            card.setCode.value,
            card.images.value.joinToString { it.value },
            card.collectionNumber.value,
            "${scryfall.eur}|${scryfall.eurFoil}|${scryfall.usd}|${scryfall.usdFoil}",
            card.finishes.value.joinToString { it.value },
        )
    }

    override fun update(cardId: CardId, properties: List<CardProperty>) {
        val arguments = properties.map { "${it.propertyName().name.lowercase()}='${it.propertyValue()}'" }.joinToString { it }
        jdbcTemplate.update(
            "UPDATE cards SET $arguments WHERE id=?",
            cardId.value,
        )
    }

    override fun update(cardId: CardId, prices: CardPrices) {
        jdbcTemplate.update(
            "UPDATE cards SET scryfall_prices=? WHERE id=?",
            "${prices.scryfall.eur}|${prices.scryfall.eurFoil}|${prices.scryfall.usd}|${prices.scryfall.usdFoil}",
            cardId.value,
        )
    }

    override fun update(cardId: CardId, isOwned: CardIsOwned, isOwnedFoil: CardIsOwnedFoil) {
        jdbcTemplate.update(
            "UPDATE cards SET is_owned=?, is_owned_foil=? WHERE id=?",
            isOwned.value,
            isOwnedFoil.value,
            cardId.value,
        )
    }

    override fun get(id: CardId) = jdbcTemplate.queryForObject("SELECT * FROM cards WHERE id=?", CardRowMapper(), id.value)

    override fun getAll(code: SetCode) = jdbcTemplate.query("SELECT * FROM cards WHERE set_code=?", CardRowMapper(), code.value)

    override fun getCollection() = jdbcTemplate.query("SELECT * FROM cards WHERE is_owned=true", CardRowMapper())
}
