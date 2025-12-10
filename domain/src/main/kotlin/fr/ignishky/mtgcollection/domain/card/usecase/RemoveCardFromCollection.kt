package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.event.CardDisowned
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import jakarta.inject.Named

class RemoveCardFromCollection(
    val cardId: CardId,
    val isOwnedFoil: Boolean,
) : Command

@Named
class RemoveCardFromCollectionHandler(
    private val cardProjection: CardProjectionPort,
) : CommandHandler<RemoveCardFromCollection> {

    override fun handle(command: Command): List<Event<*, *, *>> {
        command as RemoveCardFromCollection
        return cardProjection.get(command.cardId)
            ?.run {
                cardProjection.update(
                    this.id,
                    if (!command.isOwnedFoil) this.nbOwnedNonFoil.decrease() else this.nbOwnedNonFoil,
                    if (command.isOwnedFoil) this.nbOwnedFoil.decrease() else this.nbOwnedFoil,
                )
            }
            ?.let { listOf(CardDisowned(command.cardId)) }
            ?: emptyList()
    }

    override fun listenTo() = RemoveCardFromCollection::class

}
