package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.TYPE

data class SetType(
    val value: String,
) : SetProperty {

    override fun propertyName() = TYPE

    override fun propertyValue() = value

}
