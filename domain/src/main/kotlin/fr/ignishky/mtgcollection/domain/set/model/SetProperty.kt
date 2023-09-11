package fr.ignishky.mtgcollection.domain.set.model

import kotlin.reflect.KClass

sealed interface SetProperty {

    fun propertyName(): PropertyName

    fun propertyValue(): String

    enum class PropertyName(private val propertyClass: KClass<out SetProperty>) {
        NAME(SetName::class),
        CODE(SetCode::class),
        TYPE(SetType::class),
        ICON(SetIcon::class),
        RELEASED_AT(SetReleasedAt::class);

        fun withValue(value: String): SetProperty {
            return propertyClass.constructors.first().call(value)
        }
    }
}
