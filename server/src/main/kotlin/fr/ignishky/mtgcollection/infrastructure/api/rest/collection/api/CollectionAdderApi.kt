package fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RequestMapping("/collection/{cardId}")
@Tag(name = "Collection", description = "All the needed endpoints to manipulate a collection")
fun interface CollectionAdderApi {

    @PutMapping(consumes = [APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Add the card with the given id to the collection",
        description = "The card finish is provided inside the request body",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "OK - The card has been added to the collection"),
        ],
    )
    @ResponseStatus(NO_CONTENT)
    fun addCard(
        @PathVariable cardId: String,
        @RequestBody ownedBody: OwnedBody,
    )

}

data class OwnedBody(
    val ownedFoil: Boolean,
) {
    @Suppress("unused")
    constructor() : this(false)
}
