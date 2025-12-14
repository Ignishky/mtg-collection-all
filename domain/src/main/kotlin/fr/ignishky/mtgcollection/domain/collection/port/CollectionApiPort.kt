package fr.ignishky.mtgcollection.domain.collection.port

import fr.ignishky.mtgcollection.domain.card.model.Card

fun interface CollectionApiPort {

    fun getAll(): List<Card>

}