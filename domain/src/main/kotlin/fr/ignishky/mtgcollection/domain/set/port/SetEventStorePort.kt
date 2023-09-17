package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.Set

interface SetEventStorePort {

    fun getAll(): List<Set>

}
