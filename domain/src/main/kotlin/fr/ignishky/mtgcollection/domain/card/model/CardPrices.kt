package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.PRICES

data class CardPrices(
    val scryfall: Price,
) : CardProperty {
    override fun propertyName(): CardProperty.PropertyName {
        return PRICES
    }

    override fun propertyValue(): Any {
        return scryfall
    }
}
