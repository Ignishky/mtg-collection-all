package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface CardRefererPort {

    fun getCards(code: SetCode): List<Card>

}
