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
    private val cardProjection: CardProjectionPort,
) : CommandHandler<AddCardToCollection> {

    override fun handle(command: Command): List<Event<*, *, *>> {
        command as AddCardToCollection
        return cardProjection.get(command.cardId)
            ?.run {
                cardProjection.update(
                    command.cardId,
                    CardIsOwned(true),
                    this.nbOwned.increment(),
                    command.isOwnedFoil,
                    if (command.isOwnedFoil.value) this.nbOwnedFoil.increment() else this.nbOwnedFoil,
                )
            }
            ?.let { listOf(CardOwned(command.cardId, command.isOwnedFoil)) }
            ?: emptyList()
    }

    override fun listenTo() = AddCardToCollection::class

}
