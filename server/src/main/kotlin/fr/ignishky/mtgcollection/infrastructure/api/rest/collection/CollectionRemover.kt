package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.usecase.RemoveCardFromCollection
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionRemover(
    private val commandBus: CommandBus,
) : CollectionRemoverApi {

    override fun removeCard(cardId: String) {
        commandBus.dispatch(RemoveCardFromCollection(CardId(cardId)))
    }

}
