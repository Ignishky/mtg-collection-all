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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging.logger

class RefreshCard : Command

@Named
class RefreshCardHandler(
    private val setProjection: SetProjectionPort,
    private val cardReferer: CardRefererPort,
    private val cardProjection: CardProjectionPort,
) : CommandHandler<RefreshCard> {

    private val logger = logger {}

    override fun handle(command: Command): List<Event<CardId, Card, out Payload>> {
        val sets = setProjection.getAll()
        return runBlocking {
            sets.map { set ->
                async(Dispatchers.Default) {
                    processSet(set)
                }
            }.awaitAll().flatten()
        }
    }

    private fun processSet(set: Set): List<Event<CardId, Card, out Payload>> {
        logger.info { "Refreshing cards from ${set.code.value} ..." }
        val knownCardsById = cardProjection.getAll(set.code).associateBy { it.id }
        return cardReferer.getCards(set.code)
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
