package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.IMAGES

data class CardImages(
    val value: List<CardImage>
) : CardProperty {

    constructor(image: CardImage) : this(listOf(image))

    override fun propertyName(): CardProperty.PropertyName {
        return IMAGES
    }

    override fun propertyValue(): Any {
        return value
    }
}
