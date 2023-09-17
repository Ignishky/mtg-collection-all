package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.CODE

data class SetCode(
    val value: String,
) : SetProperty {

    override fun propertyName() = CODE

    override fun propertyValue() = value

}
