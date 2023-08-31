package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.TYPE

data class SetType(
    val value: String
) : SetProperty {

    override fun propertyName(): SetProperty.PropertyName {
        return TYPE
    }

    override fun propertyValue(): Any {
        return value
    }

}
