package fr.ignishky.mtgcollection.domain.card.model

sealed interface CardProperty {

    fun propertyName(): PropertyName

    fun propertyValue(): String

    enum class PropertyName {
        NAME,
        SET_CODE,
        IMAGES,
        COLLECTION_NUMBER;

        fun withValue(value: String) = when (this) {
            NAME -> CardName(value)
            SET_CODE -> CardSetCode(value)
            IMAGES -> CardImages((value).split(", ").map { CardImage(it) })
            COLLECTION_NUMBER -> CardNumber(value)
        }
    }

}
