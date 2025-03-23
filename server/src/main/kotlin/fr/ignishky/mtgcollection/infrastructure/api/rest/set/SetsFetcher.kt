package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import mu.KotlinLogging
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SetsFetcher(
    private val setApi: SetApiPort,
) : SetsFetcherApi {

    private val logger = KotlinLogging.logger {}

    override fun getAll(): ResponseEntity<SetsResponse> {
        logger.info { "Requesting all sets ..." }

        val sets = setApi.getAll()
            .map { SetResponse(it.code.value, it.name.value, it.type.value, it.icon.value) }

        logger.info { "Returning ${sets.size} sets." }
        return ResponseEntity(SetsResponse(sets), OK)
    }

}
