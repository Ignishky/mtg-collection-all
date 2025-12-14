package fr.ignishky.mtgcollection.domain.collection.model

import fr.ignishky.mtgcollection.domain.card.model.Card

data class CardCollection(
    val cards: List<Card>,
    val size: Int,
    val value: Long,
)
