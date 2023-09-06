package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.exception.NoCardFoundException
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface CardApiPort {

    @Throws(NoCardFoundException::class)
    fun getAll(setCode: SetCode): List<Card>

}
