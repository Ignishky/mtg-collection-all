package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetProperty

interface SetStorePort {

    fun add(set: Set)

    fun update(setId: SetId, properties: List<SetProperty>)

    fun getAll(): List<Set>

    fun get(id: SetId): Set

}
