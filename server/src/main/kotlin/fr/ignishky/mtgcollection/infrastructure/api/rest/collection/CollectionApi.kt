package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/collection")
@Tag(name = "Collection", description = "All the needed endpoints to manipulate a collection")
interface CollectionApi {

    @PutMapping("/{cardId}", consumes = [APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Add the card with the given id to the collection",
        description = "The card finish is provided inside the request body",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - The card has been added to the collection"),
        ],
    )
    fun addCard(
        @PathVariable cardId: String,
        @RequestBody ownedBody: OwnedBody,
    )

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
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

data class OwnedBody(
    val ownedFoil: Boolean,
) {
    @Suppress("unused")
    constructor() : this(false)
}
