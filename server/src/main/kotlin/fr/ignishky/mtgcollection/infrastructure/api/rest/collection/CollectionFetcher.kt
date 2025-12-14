package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import fr.ignishky.mtgcollection.domain.collection.port.CollectionApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.CollectionFetcherApi
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.PricesResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionFetcher(
    private val collectionApi: CollectionApiPort,
) : CollectionFetcherApi {

    override fun getCollection(): ResponseEntity<CollectionResponse> {
        val (cards, size, value) = collectionApi.getCollection()
        val cardResponses = cards
            .map {
                CardResponse(
                    it.id.value,
                    it.name.value,
                    it.images.value[0].value,
                    it.finishes.isFoil(),
                    it.finishes.isNonFoil(),
                    PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil),
                    it.nbOwnedNonFoil.value,
                    it.nbOwnedFoil.value,
                )
            }

        return ok(CollectionResponse(cardResponses, size, value))
    }
}

@JsonInclude(NON_NULL)
data class CollectionResponse(
    val cards: List<CardResponse>,
    val size: Int,
    val value: Long,
)
