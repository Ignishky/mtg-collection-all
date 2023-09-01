package fr.ignishky.mtgcollection.infrastructure.api.rest.refresh

import fr.ignishky.framework.domain.CorrelationId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/refresh-all")
interface RefreshApi {

    @PutMapping
    fun loadAll(@RequestAttribute correlationId: CorrelationId): ResponseEntity<Void>

}
