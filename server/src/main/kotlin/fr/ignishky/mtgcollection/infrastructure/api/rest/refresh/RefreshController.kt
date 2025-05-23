package fr.ignishky.mtgcollection.infrastructure.api.rest.refresh

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.mtgcollection.domain.card.usecase.RefreshCard
import fr.ignishky.mtgcollection.domain.set.usecase.RefreshSet
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class RefreshController(
    private val commandBus: CommandBus,
) : RefreshApi {

    override fun loadAll(): ResponseEntity<Unit> {
        commandBus.dispatch(RefreshSet())
        commandBus.dispatch(RefreshCard())
        return ResponseEntity(NO_CONTENT)
    }

}
