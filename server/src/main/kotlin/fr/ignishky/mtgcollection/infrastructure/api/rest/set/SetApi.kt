package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.framework.domain.CorrelationId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/sets")
@Tag(name = "Card Sets", description = "All the needed endpoints to manipulate card sets")
interface SetApi {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Retrieve the list of all the sets",
        description = "The list is sorted from the most recent set to the oldest.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - The sets list")
        ],
    )
    fun getAll(@RequestAttribute correlationId: CorrelationId): SetsResponse

    @GetMapping("/{setCode}/cards", produces = [APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Retrieve the cards from the set of the given code",
        description = "The list is sorted by the cards collection number.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - The cards list"),
            ApiResponse(
                responseCode = "404",
                description = "NOT FOUND - The given setCode is not valid",
                content = [Content()],
            )
        ],
    )
    fun getCards(@RequestAttribute correlationId: CorrelationId, @PathVariable setCode: String): CardsResponse

}
