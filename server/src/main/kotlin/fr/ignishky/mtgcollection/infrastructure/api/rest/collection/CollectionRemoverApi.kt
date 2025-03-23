package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/collection/{cardId}")
@Tag(name = "Collection", description = "All the needed endpoints to manipulate a collection")
fun interface CollectionRemoverApi {

    @DeleteMapping
    @Operation(
        summary = "Remove the card with the given id from the collection",
        description = "The card finish will be also reset.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "OK - The card has been removed from the collection"),
        ],
    )
    @ResponseStatus(NO_CONTENT)
    fun removeCard(
        @PathVariable cardId: String,
    )

}
