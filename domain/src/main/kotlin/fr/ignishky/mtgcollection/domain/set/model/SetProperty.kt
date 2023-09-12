package fr.ignishky.mtgcollection.domain.set.model

sealed interface SetProperty {

    fun propertyName(): PropertyName

    fun propertyValue(): String

    enum class PropertyName {
        NAME,
        CODE,
        TYPE,
        ICON,
        RELEASED_AT,
    }
}
