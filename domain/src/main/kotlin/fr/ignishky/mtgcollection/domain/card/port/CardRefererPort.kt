package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.SetCode

fun interface CardRefererPort {

    fun getCards(setCode: SetCode): List<Card>

}
