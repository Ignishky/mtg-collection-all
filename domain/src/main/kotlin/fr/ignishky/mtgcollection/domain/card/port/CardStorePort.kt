package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.model.CardProperty
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface CardStorePort {

    fun add(card: Card)

    fun update(cardId: CardId, properties: List<CardProperty>)

    fun update(cardId: CardId, prices: CardPrices)

    fun get(id: CardId): Card

    fun getAll(code: SetCode): List<Card>

}
