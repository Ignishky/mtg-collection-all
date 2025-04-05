package fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api

import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.CollectionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/collection")
@Tag(name = "Collection", description = "All the needed endpoints to manipulate a collection")
fun interface CollectionFetcherApi {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get the full list of cards in the collections",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - The list of cards in the collection")
        ]
    )
    fun getCollection(): ResponseEntity<CollectionResponse>
}
