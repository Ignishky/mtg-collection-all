package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.collection.usecase.AddCardToCollection
import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.CollectionAdderApi
import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.dto.OwnedBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionAdder(
    private val commandBus: CommandBus,
) : CollectionAdderApi {

    override fun addCard(cardId: String, ownedBody: OwnedBody) {
        commandBus.dispatch(AddCardToCollection(CardId(cardId), ownedBody.ownedFoil))
    }

}
