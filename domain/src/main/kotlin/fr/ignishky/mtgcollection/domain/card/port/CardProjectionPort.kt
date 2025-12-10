package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.set.model.SetCode

interface CardProjectionPort {

    fun add(card: Card)

    fun update(cardId: CardId, properties: List<CardProperty>)

    fun update(cardId: CardId, prices: CardPrices)

    fun update(
        cardId: CardId,
        nbOwnedNonFoil: CardNbOwnedNonFoil,
        nbOwnedFoil: CardNbOwnedFoil,
    )

    fun get(id: CardId): Card?

    fun getAll(code: SetCode): List<Card>

    fun getCollection(): List<Card>
}
