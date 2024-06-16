package fr.ignishky.mtgcollection.domain.card.model

data class CardPrices(
    val scryfall: Price,
) {
    fun update(updateCardPrices: CardPrices): CardPrices {
        return CardPrices(scryfall.update(updateCardPrices.scryfall))
    }
}
