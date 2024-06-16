package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.port.CollectionApiPort
import fr.ignishky.mtgcollection.domain.card.usecase.AddCardToCollection
import fr.ignishky.mtgcollection.domain.card.usecase.RemoveCardFromCollection
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.PricesResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

@RestController
class CollectionController(
    private val commandBus: CommandBus,
    private val collectionApi: CollectionApiPort,
) : CollectionApi {

    override fun addCard(cardId: String, ownedBody: OwnedBody) {
        commandBus.dispatch(AddCardToCollection(CardId(cardId), CardIsOwnedFoil(ownedBody.ownedFoil)))
    }

    override fun removeCard(cardId: String) {
        commandBus.dispatch(RemoveCardFromCollection(CardId(cardId)))
    }

    override fun getCollection(): ResponseEntity<CollectionResponse> {
        val cards = collectionApi.getAll()
        val cardResponses = cards
            .map {
                CardResponse(
                    it.id.value,
                    it.name.value,
                    it.images.value[0].value,
                    it.finishes.isFoil(),
                    it.finishes.isNonFoil(),
                    PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil),
                    it.isOwned.value,
                    it.isOwnedFoil.value,
                )
            }

        val pricesResponse = cards.fold(PricesResponse(0, 0)) { (eur), card ->
            PricesResponse(eur + if (card.isOwnedFoil.value) card.prices.scryfall.eurFoil else card.prices.scryfall.eur, 0)
        }
        return ok(CollectionResponse(pricesResponse, cardResponses))
    }
}
