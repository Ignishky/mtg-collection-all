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
import jakarta.inject.Named
import mu.KotlinLogging.logger

class RefreshCard : Command

@Named
class RefreshCardHandler(
    private val cardReferer: CardRefererPort,
    private val cardProjection: CardProjectionPort,
) : CommandHandler<RefreshCard> {

    private val logger = logger {}

    override fun handle(command: Command): List<Event<CardId, Card, out Payload>> {
        logger.info { "Downloading all cards..." }
        val knownCardsById = cardProjection.getAll().associateBy { it.id }
        return cardReferer.getAllCards()
            .also { logger.info { "Processing all cards..." } }
            .flatMap { card ->
                if (!knownCardsById.contains(card.id)) {
                    runCatching {
                        createCard(card)
                    }.fold(
                        onSuccess = { it },
                        onFailure = {
                            cardProjection.get(card.id)?.let { updateCard(it, card) } ?: handleFallbackFailure(it, card)
                        }
                    )
                } else {
                    updateCard(knownCardsById[card.id]!!, card)
                }
            }
    }

    private fun handleFallbackFailure(t: Throwable, card: Card): List<Event<CardId, Card, out Payload>> {
        logger.error(t) { "Error while creating card ${card.id.value}" }
        return emptyList()
    }

    private fun createCard(card: Card): List<CardCreated> {
        cardProjection.add(card)
        return listOf(CardCreated(card))
    }

    private fun updateCard(knownCard: Card, updatedCard: Card): List<Event<CardId, Card, out Payload>> {
        var events = emptyList<Event<CardId, Card, out Payload>>()
        val delta = knownCard.updatedCardProperties(updatedCard)
        if (delta.isNotEmpty()) {
            events = events.plus(CardUpdated(updatedCard.id, *delta.toTypedArray()))
            cardProjection.update(knownCard.id, delta)
        }
        if (updatedCard.hasPrices() && knownCard.prices != updatedCard.prices) {
            val updatedPrices = knownCard.prices.update(updatedCard.prices)
            events = events.plus(CardPricesUpdated(updatedCard.id, updatedPrices))
            cardProjection.update(knownCard.id, updatedPrices)
        }
        return events
    }

    override fun listenTo() = RefreshCard::class

}
