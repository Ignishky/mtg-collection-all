package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.mtgcollection.domain.set.model.SetProperty.PropertyName.RELEASED_AT
import java.time.LocalDate

data class SetReleasedAt(
    val value: LocalDate,
) : SetProperty, Comparable<SetReleasedAt> {

    override fun compareTo(other: SetReleasedAt) = value.compareTo(other.value)

    override fun propertyName() = RELEASED_AT

    override fun propertyValue() = value.toString()

}
