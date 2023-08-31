package fr.ignishky.mtgcollection.domain.card.model

sealed interface CardProperty {

    fun propertyName(): PropertyName

    fun propertyValue(): Any

    enum class PropertyName {
        NAME,
        SET_CODE,
        IMAGES,
        COLLECTION_NUMBER,
        PRICES,
    }
}
