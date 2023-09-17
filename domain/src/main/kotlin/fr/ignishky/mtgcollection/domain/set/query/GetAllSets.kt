package fr.ignishky.mtgcollection.domain.set.query

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named

@Named
class GetAllSets(
    private val setStore: SetStorePort,
) : SetApiPort {

    override fun getAll() = setStore.getAll()
        .filter { hasOwnIcon(it) }
        .sortedByDescending { it.releasedAt }

    private fun hasOwnIcon(it: Set) = it.icon.value.contains(it.code.value)

}
