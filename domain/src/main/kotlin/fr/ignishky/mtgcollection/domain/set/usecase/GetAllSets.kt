package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import jakarta.inject.Named

@Named
class GetAllSets(
    private val setProjectionPort: SetProjectionPort,
) : SetApiPort {

    override fun getAll() = setProjectionPort.getAll()
        .filter { hasOwnIcon(it) }
        .sortedByDescending { it.releasedAt }

    private fun hasOwnIcon(it: Set) = it.icon.value.contains(it.code.value)

    override fun get(setCode: SetCode): Set? {
        return setProjectionPort.get(setCode)
    }
}
