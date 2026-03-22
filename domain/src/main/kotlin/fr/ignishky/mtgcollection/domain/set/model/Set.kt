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
    val parentCode: SetParentCode?,
    val nbCards: SetNbCards,
    val nbOwnedCards: SetNbOwnedCards,
) : Aggregate<SetId> {

    override fun id() = id

    fun updatedFields(newSet: Set): List<SetProperty> {
        var result = emptyList<SetProperty>()
        for (prop in Set::class.memberProperties) {
            prop.call(newSet)?.let {
                if (it is SetProperty && prop.call(this) != it) {
                    result = result.plus(it)
                }
            }
        }
        return result
    }

}
