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
) : Aggregate<CardId> {

    constructor() : this(
        CardId(""),
        CardName(""),
        CardSetCode(""),
        CardImages(emptyList()),
        CardNumber(""),
        CardPrices(Price(0, 0, 0, 0)),
    )

    override fun id(): CardId {
        return id
    }

    fun updatedFields(newCard: Card): List<CardProperty> {
        var result = listOf<CardProperty>()
        for (prop in Card::class.memberProperties) {
            val newProperty = prop.call(newCard)!!
            if (CardProperty::class.java.isAssignableFrom(newProperty::class.java) && prop.call(this) != newProperty) {
                result = result.plus(newProperty as CardProperty)
            }
        }
        return result
    }

}
