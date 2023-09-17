package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import fr.ignishky.mtgcollection.domain.card.model.*
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CardRowMapper : RowMapper<Card> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Card {
        val prices = rs.getString("scryfall_prices").split('|').map { it.toLong() }
        return Card(
            CardId(rs.getString("id")),
            CardName(rs.getString("name")),
            CardSetCode(rs.getString("set_code")),
            CardImages(rs.getString("images").split(", ").map { CardImage(it) }),
            CardNumber(rs.getString("collection_number")),
            CardPrices(Price(prices[0], prices[1], prices[2], prices[3])),
        )
    }

}
