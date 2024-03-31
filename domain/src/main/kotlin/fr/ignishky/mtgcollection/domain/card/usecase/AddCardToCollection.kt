package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import jakarta.inject.Named

data class AddCardToCollection(
    val cardId: CardId,
    val isOwnedFoil: CardIsOwnedFoil,
) : Command

@Named
class AddCardToCollectionHandler(
    private val cardProjectionPort: CardProjectionPort,
) : CommandHandler<AddCardToCollection> {

    override fun handle(command: Command): List<Event<*, *, *>> {
        command as AddCardToCollection
        return cardProjectionPort.get(command.cardId)
            ?.run { cardProjectionPort.update(command.cardId, CardIsOwned(true), command.isOwnedFoil) }
            ?.let { listOf(CardOwned(command.cardId, command.isOwnedFoil)) }
            ?: emptyList()
    }

    override fun listenTo() = AddCardToCollection::class

}
