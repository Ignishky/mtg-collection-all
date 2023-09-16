package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated.CardCreatedHandler
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
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
    )

    @Test
    fun `Should apply event to card`() {
        val result = event.apply(emptyCard)

        assertThat(result).isEqualTo(
            emptyCard.copy(
                CardId("cardId"),
                CardName("cardName"),
                CardSetCode("setCode"),
                CardImages(listOf(CardImage("cardImage"))),
                CardNumber("collectionNumber"),
                CardPrices(Price(110, 220, 330, 440)),
            )
        )
    }

    private val cardStore = mockk<CardStorePort>()
    private val handler = CardCreatedHandler(cardStore)

    @Test
    fun `Should handle created event`() {
        justRun {
            cardStore.store(
                emptyCard.copy(
                    CardId("cardId"),
                    CardName("cardName"),
                    CardSetCode("setCode"),
                    CardImages(listOf(CardImage("cardImage"))),
                    CardNumber("collectionNumber"),
                    CardPrices(Price(110, 220, 330, 440)),
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
