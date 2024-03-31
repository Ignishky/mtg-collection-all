package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.framework.domain.Aggregate
import kotlin.reflect.full.memberProperties

data class Set(
    val id: SetId,
    val code: SetCode,
    val name: SetName,
    val type: SetType,
    val icon: SetIcon,
    val releasedAt: SetReleasedAt,
) : Aggregate<SetId> {

    override fun id() = id

    fun updatedFields(newSet: Set): List<SetProperty> {
        var result = emptyList<SetProperty>()
        for (prop in Set::class.memberProperties) {
            val newProperty = prop.call(newSet)!!
            if (prop.call(this) != newProperty) {
                result = result.plus(newProperty as SetProperty)
            }
        }
        return result
    }

}
