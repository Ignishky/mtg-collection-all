package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface CardStorePort {

    fun store(card: Card)

    fun get(id: CardId): Card

    fun get(code: SetCode): List<Card>

}
