package fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api

import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.dto.OwnedBody
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/collection/{cardId}/remove")
@Tag(name = "Collection", description = "All the needed endpoints to manipulate a collection")
fun interface CollectionRemoverApi {

    @PostMapping
    @Operation(
        summary = "Remove the card with the given id from the collection",
        description = "The card finish will be also reset.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "OK - The card has been removed from the collection"),
            ApiResponse(responseCode = "400", description = "Bad request - The card was not in the collection."),
        ],
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeCard(
        @PathVariable cardId: String,
        @RequestBody ownedBody: OwnedBody,
    )

}

