package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import jakarta.inject.Named
import mu.KotlinLogging.logger

class RefreshCard : Command

@Named
class RefreshCardHandler(
    private val setProjectionPort: SetProjectionPort,
    private val cardReferer: CardRefererPort,
    private val cardProjectionPort: CardProjectionPort,
) : CommandHandler<RefreshCard> {

    private val logger = logger {}

    override fun handle(command: Command) = setProjectionPort.getAll()
        .flatMap { set -> processSet(set) }

    private fun processSet(set: Set): List<Event<CardId, Card, out Payload>> {
        logger.info { "Refreshing cards from ${set.code.value} ..." }
        val knownCardsById = cardProjectionPort.getAll(set.code).associateBy { it.id }
        return cardReferer.getCards(set.code)
            .flatMap { card ->
                if (!knownCardsById.contains(card.id)) {
                    createCard(card)
                } else {
                    updateCard(knownCardsById[card.id]!!, card)
                }
            }
    }

    private fun createCard(card: Card): List<CardCreated> {
        cardProjectionPort.add(Card(card.id, card.name, card.setCode, card.images, card.collectionNumber,card.prices,  card.finishes))
        return listOf(CardCreated(card.id, card.name, card.setCode, card.prices, card.images, card.collectionNumber, card.finishes))
    }

    private fun updateCard(knownCard: Card, newCard: Card): List<Event<CardId, Card, out Payload>> {
        var events = emptyList<Event<CardId, Card, out Payload>>()
        val delta = knownCard.updatedFields(newCard)
        if (delta.isNotEmpty()) {
            events = events.plus(CardUpdated(newCard.id, *delta.toTypedArray()))
            cardProjectionPort.update(knownCard.id, delta)
        }
        if (knownCard.prices != newCard.prices) {
            events = events.plus(CardPricesUpdated(newCard.id, newCard.prices))
            cardProjectionPort.update(knownCard.id, newCard.prices)
        }
        return events
    }

    override fun listenTo() = RefreshCard::class

}
