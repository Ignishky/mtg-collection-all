package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.mapper

import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity

object CardEntityMapper {

    fun toCardEntity(card: Card): CardEntity {
        val scryfall = card.prices.scryfall
        return CardEntity(
            card.id.value,
            card.name.value,
            card.setCode.value,
            card.images.value.joinToString { it.value },
            card.collectionNumber.value,
            "${scryfall.eur}|${scryfall.eurFoil}|${scryfall.usd}|${scryfall.usdFoil}",
        )
    }

    fun toCard(entity: CardEntity): Card {
        val scryfallPrices = entity.scryfallPrices.split("|").map { it.toLong() }
        return Card(
            CardId(entity.id),
            CardName(entity.name),
            CardSetCode(entity.setCode),
            CardImages(entity.images.split(", ").map { CardImage(it) }),
            CardNumber(entity.collectionNumber),
            CardPrices(Price(scryfallPrices[0], scryfallPrices[1], scryfallPrices[2], scryfallPrices[3])),
        )
    }

}
