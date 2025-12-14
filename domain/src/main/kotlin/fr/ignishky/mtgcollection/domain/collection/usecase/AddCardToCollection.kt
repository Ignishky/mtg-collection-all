package fr.ignishky.mtgcollection.domain.collection.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.collection.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import jakarta.inject.Named

data class AddCardToCollection(
    val cardId: CardId,
    val isOwnedFoil: Boolean,
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
                    if (!command.isOwnedFoil) this.nbOwnedNonFoil.increment() else this.nbOwnedNonFoil,
                    if (command.isOwnedFoil) this.nbOwnedFoil.increment() else this.nbOwnedFoil,
                )
            }
            ?.let { listOf(CardOwned(command.cardId, command.isOwnedFoil)) }
            ?: emptyList()
    }

    override fun listenTo() = AddCardToCollection::class

}
