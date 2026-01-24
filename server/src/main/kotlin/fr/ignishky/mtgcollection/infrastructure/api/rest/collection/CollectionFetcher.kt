package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import fr.ignishky.mtgcollection.domain.collection.port.CollectionApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.CollectionFetcherApi
import fr.ignishky.mtgcollection.infrastructure.api.rest.common.CardResponseMapper.toCardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.CardResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionFetcher(
    private val collectionApi: CollectionApiPort,
) : CollectionFetcherApi {

    override fun getCollection(): ResponseEntity<CollectionResponse> {
        val (cards, size, value) = collectionApi.getCollection()

        return ok(
            CollectionResponse(
                cards.map { it.toCardResponse() },
                size,
                value
            )
        )
    }
}

@JsonInclude(NON_NULL)
data class CollectionResponse(
    val cards: List<CardResponse>,
    val size: Int,
    val value: Long,
)
