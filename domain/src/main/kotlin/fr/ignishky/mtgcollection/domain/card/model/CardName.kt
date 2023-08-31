package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.NAME

data class CardName(
    val value: String
) : CardProperty {
    override fun propertyName(): CardProperty.PropertyName {
        return NAME
    }

    override fun propertyValue(): Any {
        return value
    }
}
