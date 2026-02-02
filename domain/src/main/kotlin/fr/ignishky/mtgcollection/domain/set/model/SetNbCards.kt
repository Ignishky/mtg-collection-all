package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.NB_CARDS

data class SetNbCards(
    val value: Int,
) : SetProperty {
    override fun propertyName() = NB_CARDS

    override fun propertyValue() = value.toString()
}
