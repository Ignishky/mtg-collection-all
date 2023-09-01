package fr.ignishky.mtgcollection.infrastructure.api.rest.refresh

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.command.RefreshCard
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class RefreshController(
    private val commandBus: CommandBus,
) : RefreshApi {

    override fun loadAll(correlationId: CorrelationId): ResponseEntity<Void> {
        commandBus.dispatch(RefreshSet(), correlationId)
        commandBus.dispatch(RefreshCard(), correlationId)
        return ResponseEntity(NO_CONTENT)
    }

}
