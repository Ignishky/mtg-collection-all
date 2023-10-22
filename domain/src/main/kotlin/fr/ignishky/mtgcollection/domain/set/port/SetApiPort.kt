package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface SetApiPort {

    fun getAll(): List<Set>

    fun get(setCode: SetCode): Set?

}
