package fr.ignishky.mtgcollection.domain.card.model

sealed interface CardProperty {

    fun propertyName(): PropertyName

    fun propertyValue(): Any

    enum class PropertyName {
        NAME,
        SET_CODE,
        IMAGES,
        COLLECTION_NUMBER;

        fun withValue(value: Any): CardProperty {
            return when(this) {
                NAME -> CardName(value as String)
                SET_CODE -> CardSetCode(value as String)
                IMAGES -> CardImages((value as List<String>).map { CardImage(it) })
                COLLECTION_NUMBER -> CardNumber(value as String)
            }
        }
    }
}
