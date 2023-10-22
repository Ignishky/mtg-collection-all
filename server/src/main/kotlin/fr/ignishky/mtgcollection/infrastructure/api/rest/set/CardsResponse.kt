package fr.ignishky.mtgcollection.infrastructure.api.rest.set

data class CardsResponse(
    val name: String,
    val cards: List<CardResponse>,
    val prices: PricesResponse,
)

data class CardResponse(
    val id: String,
    val name: String,
    val image: String,
    val isFoil: Boolean,
    val isNonFoil: Boolean,
    val prices: PricesResponse,
)

data class PricesResponse(
    val eur: Long,
    val eurFoil: Long,
)
