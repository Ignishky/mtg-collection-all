package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.framework.domain.Aggregate
import java.time.LocalDate.EPOCH
import kotlin.reflect.full.memberProperties

data class Set(
    val id: SetId,
    val code: SetCode,
    val name: SetName,
    val type: SetType,
    val icon: SetIcon,
    val releasedAt: SetReleasedAt,
) : Aggregate<SetId> {

    constructor() : this(SetId(""), SetCode(""), SetName(""), SetType(""), SetIcon(""), SetReleasedAt(EPOCH))

    override fun id(): SetId {
        return id
    }

    fun updatedFields(newSet: Set): List<SetProperty> {
        var result = listOf<SetProperty>()
        for (prop in Set::class.memberProperties) {
            val newProperty = prop.call(newSet)!!
            if (prop.call(this) != newProperty) {
                result = result.plus(newProperty as SetProperty)
            }
        }
        return result
    }

}
