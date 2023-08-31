package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.Set

interface SetRefererPort {

    fun getAllSets(): List<Set>

}
