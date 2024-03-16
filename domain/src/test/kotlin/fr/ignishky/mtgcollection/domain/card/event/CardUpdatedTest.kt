package fr.ignishky.mtgcollection.domain.card.event

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

    private val updatedCard = plus2Mace.copy(
        name = CardName("cardName"),
        setCode = CardSetCode("cardSetCode"),
        images = CardImages(listOf(CardImage("cardImage"))),
        collectionNumber = CardNumber("collectionNumber"),
        finishes = CardFinishes(listOf(CardFinish("foil"))),
    )

    private val event = CardUpdated(
        plus2Mace.id,
        CardName("cardName"),
        CardSetCode("cardSetCode"),
        CardImages(listOf(CardImage("cardImage"))),
        CardNumber("collectionNumber"),
        CardFinishes(listOf(CardFinish("foil"))),
    )

    @Test
    fun `should apply event to card`() {
        val result = event.apply(plus2Mace)

        assertThat(result).isEqualTo(updatedCard)
    }

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val handler = CardUpdatedHandler(cardProjectionPort)

    @Test
    fun `should handle full updated card event`() {
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
    fun `should handle updated card event`(property: CardProperty) {
        justRun { cardProjectionPort.update(plus2Mace.id, listOf(property)) }

        handler.handle(CardUpdated(plus2Mace.id, property))

        verify { cardProjectionPort.update(plus2Mace.id, listOf(property)) }
    }

    @Test
    fun `handler should listen to CardCreate`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(CardUpdated::class)
    }
}
