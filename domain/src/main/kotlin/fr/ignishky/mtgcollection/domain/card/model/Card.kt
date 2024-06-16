package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.framework.domain.Aggregate
import kotlin.reflect.full.memberProperties

data class Card(
    val id: CardId,
    val name: CardName,
    val setCode: CardSetCode,
    val images: CardImages,
    val collectionNumber: CardNumber,
    val prices: CardPrices,
    val finishes: CardFinishes,
    val isOwned: CardIsOwned = CardIsOwned(false),
    val isOwnedFoil: CardIsOwnedFoil = CardIsOwnedFoil(false),
) : Aggregate<CardId> {

    override fun id() = id

    fun updatedCardProperties(newCard: Card): List<CardProperty> {
        var result = emptyList<CardProperty>()
        for (prop in Card::class.memberProperties) {
            val newProperty = prop.call(newCard)!!
            if (CardProperty::class.java.isAssignableFrom(newProperty::class.java) && prop.call(this) != newProperty) {
                result = result.plus(newProperty as CardProperty)
            }
        }
        return result
    }

    fun hasPrices(): Boolean {
        val scryfallPrices = prices.scryfall
        return scryfallPrices.eur > 0 || scryfallPrices.eurFoil > 0 || scryfallPrices.usd > 0 || scryfallPrices.usdFoil > 0
    }

    fun minEurPrice(): Long {
        return if (finishes.isNonFoil()) {
            prices.scryfall.eur
        } else {
            prices.scryfall.eurFoil
        }
    }

    fun maxEurPrice(): Long {
        return if (finishes.isFoil()) {
            prices.scryfall.eurFoil
        } else {
            prices.scryfall.eur
        }
    }

}
