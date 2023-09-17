package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface CardStorePort {

    fun add(card: Card)

    fun update(card: Card)

    fun get(id: CardId): Card

    fun getAll(code: SetCode): List<Card>

}
