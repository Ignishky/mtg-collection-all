package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedHandler
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CardUpdatedTest {

    private val existingCard = plus2Mace()
    private val updatedCard = existingCard.copy(
        name = CardName("cardName"),
        images = CardImages(listOf(CardImage("cardImage"))),
        collectionNumber = CardNumber("collectionNumber"),
    )

    private val event = CardUpdated(
        CorrelationId("CardUpdated_CorrelationId"),
        existingCard.id,
        CardName("cardName"),
        CardImages(listOf(CardImage("cardImage"))),
        CardNumber("collectionNumber"),
    )

    @Test
    fun `Should apply event to card`() {
        val result = event.apply(existingCard)

        assertThat(result).isEqualTo(updatedCard)
    }

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val handler = CardUpdatedHandler(cardProjectionPort)

    @Test
    fun `Should handle full updated card event`() {
        justRun {
            cardProjectionPort.update(
                updatedCard.id, listOf(
                    CardName("cardName"),
                    CardImages(listOf(CardImage("cardImage"))),
                    CardNumber("collectionNumber"),
                )
            )
        }

        handler.handle(event)

        verify {
            cardProjectionPort.update(
                updatedCard.id, listOf(
                    CardName("cardName"),
                    CardImages(listOf(CardImage("cardImage"))),
                    CardNumber("collectionNumber"),
                )
            )
        }
    }

    @Test
    fun `Should handle only name updated card event`() {
        justRun { cardProjectionPort.update(existingCard.id, listOf(CardName("updatedName"))) }

        handler.handle(CardUpdated(CorrelationId("CardUpdated_CorrelationId"), existingCard.id, CardName("updatedName")))

        verify { cardProjectionPort.update(existingCard.id, listOf(CardName("updatedName"))) }
    }

    @Test
    fun `Should handle only set code updated card event`() {
        justRun { cardProjectionPort.update(existingCard.id, listOf(CardSetCode("updatedSetCode"))) }

        handler.handle(CardUpdated(CorrelationId("CardUpdated_CorrelationId"), existingCard.id, CardSetCode("updatedSetCode")))

        verify { cardProjectionPort.update(existingCard.id, listOf(CardSetCode("updatedSetCode"))) }
    }

    @Test
    fun `Handler should listen to CardCreate`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(CardUpdated::class)
    }
}
