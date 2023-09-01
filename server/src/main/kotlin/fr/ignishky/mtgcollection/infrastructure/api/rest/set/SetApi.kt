package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.framework.domain.CorrelationId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/sets")
interface SetApi {

    @GetMapping
    fun getAll(@RequestAttribute correlationId: CorrelationId): SetsResponse

    @GetMapping("/{setCode}")
    fun getCards(@RequestAttribute correlationId: CorrelationId, @PathVariable setCode: String): CardsResponse

}
