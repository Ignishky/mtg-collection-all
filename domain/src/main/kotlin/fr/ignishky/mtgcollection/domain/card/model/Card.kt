package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.framework.domain.Aggregate

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

}
