package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.Set

interface SetApiPort {

    fun getAll(): List<Set>

}
