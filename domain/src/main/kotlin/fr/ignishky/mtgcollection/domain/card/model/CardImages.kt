package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.IMAGES

data class CardImages(
    val value: List<CardImage>
) : CardProperty {

    constructor(image: CardImage) : this(listOf(image))

    fun containsAll(images: CardImages): Boolean {
        return value.containsAll(images.value)
    }

    override fun propertyName(): CardProperty.PropertyName {
        return IMAGES
    }

    override fun propertyValue(): Any {
        return value
    }
}
