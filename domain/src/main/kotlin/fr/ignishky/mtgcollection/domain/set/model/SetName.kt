package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.NAME

data class SetName(
    val value: String,
) : SetProperty {

    override fun propertyName() = NAME

    override fun propertyValue() = value

}
