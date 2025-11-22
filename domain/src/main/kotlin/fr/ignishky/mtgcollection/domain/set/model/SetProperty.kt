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
        RELEASED_AT,
        PARENT_CODE;

        fun withValue(value: String) = when (this) {
            NAME -> SetName(value)
            CODE -> SetCode(value)
            TYPE -> SetType(value)
            ICON -> SetIcon(value)
            RELEASED_AT -> SetReleasedAt(LocalDate.parse(value))
            PARENT_CODE -> SetParentCode(value)
        }
    }

}
