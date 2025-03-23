package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/sets")
@Tag(name = "Card Sets", description = "All the needed endpoints to manipulate card sets")
interface SetsFetcherApi {

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
    fun getAll(): ResponseEntity<SetsResponse>

}

@JsonInclude(NON_NULL)
data class SetsResponse(
    val sets: List<SetResponse>,
)

data class SetResponse(
    val code: String,
    val name: String,
    val type: String,
    val icon: String,
)
