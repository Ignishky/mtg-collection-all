package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.card.port.CollectionApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.PricesResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionFetcher(
    private val collectionApi: CollectionApiPort,
) : CollectionFetcherApi {

    override fun getCollection(): ResponseEntity<CollectionResponse> {
        val cards = collectionApi.getAll()
        val cardResponses = cards
            .map {
                CardResponse(
                    it.id.value,
                    it.name.value,
                    it.images.value[0].value,
                    it.finishes.isFoil(),
                    it.finishes.isNonFoil(),
                    PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil),
                    it.isOwned.value,
                    it.isOwnedFoil.value,
                )
            }

        val pricesResponse = cards.fold(PricesResponse(0, 0)) { (eur, eurFoil), card ->
            PricesResponse(
                eur + if (!card.isOwnedFoil.value) card.prices.scryfall.eur else 0,
                eurFoil + if (card.isOwnedFoil.value) card.prices.scryfall.eurFoil else 0
            )
        }
        return ok(CollectionResponse(pricesResponse, cardResponses))
    }
}

data class CollectionResponse(
    val prices: PricesResponse,
    val cards: List<CardResponse>
)
