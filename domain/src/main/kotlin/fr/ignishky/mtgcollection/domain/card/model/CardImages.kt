package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.IMAGES

data class CardImages(
    val value: List<CardImage>,
) : CardProperty {

    override fun propertyName() = IMAGES

    override fun propertyValue() = value.map { it.value }.joinToString { it }

}
