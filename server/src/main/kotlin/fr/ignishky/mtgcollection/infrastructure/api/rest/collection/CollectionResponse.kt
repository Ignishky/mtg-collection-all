package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.infrastructure.api.rest.set.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.PricesResponse

data class CollectionResponse(
    val prices: PricesResponse,
    val cards: List<CardResponse>
)
