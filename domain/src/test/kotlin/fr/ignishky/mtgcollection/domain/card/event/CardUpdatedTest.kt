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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CardUpdatedTest {

    private val existingCard = plus2Mace()
    private val updatedCard = existingCard.copy(
        name = CardName("cardName"),
        setCode = CardSetCode("cardSetCode"),
        images = CardImages(listOf(CardImage("cardImage"))),
        collectionNumber = CardNumber("collectionNumber"),
        finishes = CardFinishes(listOf(CardFinish("foil"))),
    )

    private val event = CardUpdated(
        CorrelationId("CardUpdated_CorrelationId"),
        existingCard.id,
        CardName("cardName"),
        CardSetCode("cardSetCode"),
        CardImages(listOf(CardImage("cardImage"))),
        CardNumber("collectionNumber"),
        CardFinishes(listOf(CardFinish("foil"))),
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
                    CardSetCode("cardSetCode"),
                    CardImages(listOf(CardImage("cardImage"))),
                    CardNumber("collectionNumber"),
                    CardFinishes(listOf(CardFinish("foil"))),
                )
            )
        }

        handler.handle(event)

        verify {
            cardProjectionPort.update(
                updatedCard.id, listOf(
                    CardName("cardName"),
                    CardSetCode("cardSetCode"),
                    CardImages(listOf(CardImage("cardImage"))),
                    CardNumber("collectionNumber"),
                    CardFinishes(listOf(CardFinish("foil"))),
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun cardPropertyProvider(): List<CardProperty> {
            return listOf(
                CardName("updatedName"),
                CardImages(listOf(CardImage("image1"))),
                CardSetCode("updatedSetCode"),
                CardNumber("updatedNumber"),
                CardFinishes(listOf(CardFinish("foil"))),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("cardPropertyProvider")
    fun `Should handle updated card event`(property: CardProperty) {
        justRun { cardProjectionPort.update(existingCard.id, listOf(property)) }

        handler.handle(CardUpdated(CorrelationId("CardUpdated_CorrelationId"), existingCard.id, property))

        verify { cardProjectionPort.update(existingCard.id, listOf(property)) }
    }

    @Test
    fun `Handler should listen to CardCreate`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(CardUpdated::class)
    }
}
