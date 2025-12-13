package fr.ignishky.mtgcollection.infrastructure.api.rest.set.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/sets")
@Tag(name = "Card Sets", description = "All the needed endpoints to manipulate card sets")
fun interface SetCardsFetcherApi {

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
    fun getCards(@PathVariable setCode: String): ResponseEntity<CardsResponse>

}

data class CardsResponse(
    val name: String,
    val cards: List<CardResponse>,
    val price: Long,
)

data class CardResponse(
    val id: String,
    val name: String,
    val image: String,
    val isFoil: Boolean,
    val isNonFoil: Boolean,
    val prices: PricesResponse,
    val nbOwnedNonFoil: Int,
    val nbOwnedFoil: Int,
)

data class PricesResponse(
    val eur: Long,
    val eurFoil: Long,
)
