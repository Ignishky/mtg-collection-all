package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.ICON

data class SetIcon(
    val value: String
) : SetProperty {

    override fun propertyName(): SetProperty.PropertyName {
        return ICON
    }

    override fun propertyValue(): String {
        return value
    }

}
