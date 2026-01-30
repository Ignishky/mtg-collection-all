package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.model.SetName

interface SetApiPort {

    fun getAll(): List<SetSummary>

    fun get(setCode: SetCode): SetSummary?

}

data class SetSummary(
    val code: SetCode,
    val name: SetName,
    val icon: SetIcon,
    val nbCards: Int,
    val nbOwnedCards: Int,
)