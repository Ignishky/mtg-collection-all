package fr.ignishky.mtgcollection.domain.set.model

import java.time.LocalDate

sealed interface SetProperty {

    fun propertyName(): PropertyName

    fun propertyValue(): String

    enum class PropertyName {
        NAME,
        CODE,
        TYPE,
        ICON,
        RELEASED_AT;

        fun withValue(value: String): SetProperty {
            return when(this) {
                NAME -> SetName(value)
                CODE -> SetCode(value)
                TYPE -> SetType(value)
                ICON -> SetIcon(value)
                RELEASED_AT -> SetReleasedAt(LocalDate.parse(value))
            }
        }
    }
}
