package fr.ignishky.mtgcollection.domain.card.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.port.CardEventStorePort
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import jakarta.inject.Named
import mu.KotlinLogging.logger

class RefreshCard : Command {

    @Named
    class RefreshCardHandler(
        private val setProjectionPort: SetProjectionPort,
        private val cardReferer: CardRefererPort,
        private val cardEventStorePort: CardEventStorePort,
    ) : CommandHandler<RefreshCard> {

        private val logger = logger {}

        override fun handle(command: Command, correlationId: CorrelationId) = setProjectionPort.getAll()
            .flatMap { set -> processSet(set, correlationId) }

        private fun processSet(set: Set, correlationId: CorrelationId): List<Event<CardId, Card, out Payload>> {
            logger.info { "Refreshing cards from ${set.code.value} ..." }
            val knownCardsById = cardEventStorePort.getAll(set.code).associateBy { it.id }
            return cardReferer.getCards(set.code)
                .flatMap {
                    if (!knownCardsById.contains(it.id)) {
                        listOf(CardCreated(correlationId, it.id, it.name, it.setCode, it.prices, it.images, it.collectionNumber, it.finishes))
                    } else {
                        cardUpdated(knownCardsById[it.id]!!, it, correlationId)
                    }
                }
        }

        private fun cardUpdated(knownCard: Card, newCard: Card, correlationId: CorrelationId): List<Event<CardId, Card, out Payload>> {
            var result = emptyList<Event<CardId, Card, out Payload>>()
            val delta = knownCard.updatedFields(newCard)
            if (delta.isNotEmpty()) {
                result = result.plus(CardUpdated(correlationId, newCard.id, *delta.toTypedArray()))
            }
            if (knownCard.prices != newCard.prices) {
                result = result.plus(CardPricesUpdated(correlationId, newCard.id, newCard.prices))
            }
            return result
        }

        override fun listenTo() = RefreshCard::class

    }

}
