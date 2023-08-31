package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.SET_CODE

data class CardSetCode(
    val value: String,
) : CardProperty {

    override fun propertyName(): CardProperty.PropertyName {
        return SET_CODE
    }

    override fun propertyValue(): Any {
        return value
    }
}
