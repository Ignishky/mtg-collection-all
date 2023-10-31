package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.infrastructure.api.rest.set.CardResponse

data class CollectionResponse(
    val cards: List<CardResponse>
)
