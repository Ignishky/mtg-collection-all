package fr.ignishky.mtgcollection.infrastructure.api.rest.set

data class CardsResponse(
    val cards: List<CardResponse>
) {

    data class CardResponse(
        val name: String,
        val image: String,
        val prices: PricesResponse,
    )

    data class PricesResponse(
        val eur: Long,
        val eurFoil: Long,
    )
}
