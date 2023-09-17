package fr.ignishky.mtgcollection.infrastructure.api.rest.refresh

import fr.ignishky.framework.domain.CorrelationId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/refresh-all")
@Tag(name = "Updater", description = "All the needed endpoints to update the application data")
interface RefreshApi {

    @PutMapping
    @Operation(summary = "Update all the data", description = "Retrieve sets, cards and prices from scryfall API")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Everything is up to date")
        ],
    )
    fun loadAll(@RequestAttribute correlationId: CorrelationId): ResponseEntity<Void>

}
