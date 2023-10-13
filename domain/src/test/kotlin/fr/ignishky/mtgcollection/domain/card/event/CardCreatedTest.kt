package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated.CardCreatedHandler
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.justRun
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CardCreatedTest {

    private val emptyCard = Card()
    private val event = CardCreated(
        CorrelationId("CardCreated_CorrelationId"),
        CardId("cardId"),
        CardName("cardName"),
        CardSetCode("setCode"),
        CardPrices(Price(110, 220, 330, 440)),
        CardImages(listOf(CardImage("cardImage"))),
        CardNumber("collectionNumber"),
        CardFinishes(listOf(CardFinish("foil"), CardFinish("nonfoil"))),
    )

    @Test
    fun `Should apply event to card`() {
        val result = event.apply(emptyCard)

        assertThat(result).isEqualTo(
            emptyCard.copy(
                id = CardId("cardId"),
                name = CardName("cardName"),
                setCode = CardSetCode("setCode"),
                images = CardImages(listOf(CardImage("cardImage"))),
                collectionNumber = CardNumber("collectionNumber"),
                prices = CardPrices(Price(110, 220, 330, 440)),
                finishes = CardFinishes(listOf(CardFinish("foil"), CardFinish("nonfoil"))),
            )
        )
    }

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val handler = CardCreatedHandler(cardProjectionPort)

    @Test
    fun `Should handle created event`() {
        justRun {
            cardProjectionPort.add(
                emptyCard.copy(
                    id = CardId("cardId"),
                    name = CardName("cardName"),
                    setCode = CardSetCode("setCode"),
                    images = CardImages(listOf(CardImage("cardImage"))),
                    collectionNumber = CardNumber("collectionNumber"),
                    prices = CardPrices(Price(110, 220, 330, 440)),
                    finishes = CardFinishes(listOf(CardFinish("foil"), CardFinish("nonfoil"))),
                )
            )
        }

        handler.handle(event)
    }

    @Test
    fun `Handler should listen to CardCreate`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(CardCreated::class)
    }
}
