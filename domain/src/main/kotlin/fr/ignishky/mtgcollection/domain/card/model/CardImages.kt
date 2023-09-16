package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.IMAGES

data class CardImages(
    val value: List<CardImage>
) : CardProperty {

    override fun propertyName(): CardProperty.PropertyName {
        return IMAGES
    }

    override fun propertyValue(): Any {
        return value.map { it.value }
    }
}
