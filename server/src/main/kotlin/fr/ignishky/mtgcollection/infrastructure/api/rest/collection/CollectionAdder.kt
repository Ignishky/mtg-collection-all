package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.usecase.AddCardToCollection
import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.CollectionAdderApi
import fr.ignishky.mtgcollection.infrastructure.api.rest.collection.api.OwnedBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionAdder(
    private val commandBus: CommandBus,
) : CollectionAdderApi {

    override fun addCard(cardId: String, ownedBody: OwnedBody) {
        commandBus.dispatch(AddCardToCollection(CardId(cardId), CardIsOwnedFoil(ownedBody.ownedFoil)))
    }

}
