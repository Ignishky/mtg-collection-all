package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.SET_CODE

data class CardSetCode(
    val value: String,
) : CardProperty {

    override fun propertyName() = SET_CODE

    override fun propertyValue() = value

}
