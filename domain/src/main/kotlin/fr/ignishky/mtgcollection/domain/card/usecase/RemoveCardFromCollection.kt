package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.event.CardDisowned
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import jakarta.inject.Named

class RemoveCardFromCollection(
    val cardId: CardId,
) : Command

@Named
class RemoveCardFromCollectionHandler(
    private val cardProjectionPort: CardProjectionPort,
) : CommandHandler<RemoveCardFromCollection> {

    override fun handle(command: Command): List<Event<*, *, *>> {
        command as RemoveCardFromCollection
        return cardProjectionPort.get(command.cardId)
            ?.run { cardProjectionPort.update(command.cardId, CardIsOwned(false), CardIsOwnedFoil(false)) }
            ?.let { listOf(CardDisowned(command.cardId)) }
            ?: emptyList()
    }

    override fun listenTo() = RemoveCardFromCollection::class

}
