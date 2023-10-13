package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.FINISHES

data class CardFinishes(
    val value: List<CardFinish>,
) : CardProperty {

    override fun propertyName(): CardProperty.PropertyName = FINISHES

    override fun propertyValue(): String = value.map { it.value }.joinToString { it }

    fun isFoil(): Boolean = value.contains(CardFinish("foil"))

    fun isNonFoil(): Boolean = value.contains(CardFinish("nonfoil"))
}

data class CardFinish(
    val value: String,
)
