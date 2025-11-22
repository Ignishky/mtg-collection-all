package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.PARENT_CODE

data class SetParentCode(
    val value: String,
) : SetProperty {

    override fun propertyName() = PARENT_CODE

    override fun propertyValue() = value

}
