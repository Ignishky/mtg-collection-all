package fr.ignishky.mtgcollection.domain.collection.port

import fr.ignishky.mtgcollection.domain.collection.model.CardCollection

fun interface CollectionApiPort {

    fun getCollection(): CardCollection

}