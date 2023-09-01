package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedHandler
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.fixture.CardFixtures.plus2Mace
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CardUpdatedTest {

    private val existingCard = plus2Mace()
    private val updatedCard = existingCard.copy(
        name = CardName("cardName"),
        images = CardImages(CardImage("cardImage")),
        collectionNumber = CardNumber("collectionNumber"),
        prices = CardPrices(Price(550, 660, 770, 880)),
    )

    private val event = CardUpdated(
        existingCard.id,
        CardName("cardName"),
        CardPrices(Price(550, 660, 770, 880)),
        CardImages(CardImage("cardImage")),
        CardNumber("collectionNumber"),
    )

    @Test
    fun `Should apply event to card`() {
        val result = event.apply(existingCard)

        assertThat(result).isEqualTo(updatedCard)
    }

    private val cardStore = mockk<CardStorePort>()
    private val handler = CardUpdatedHandler(cardStore)

    @Test
    fun `Should handle full updated card event`() {
        every { cardStore.get(existingCard.id) } returns existingCard
        justRun { cardStore.store(updatedCard) }

        handler.handle(event)

        verify { cardStore.store(updatedCard) }
    }

    @Test
    fun `Should handle only name updated card event`() {
        every { cardStore.get(existingCard.id) } returns existingCard
        justRun { cardStore.store(existingCard.copy(name = CardName("updatedName"))) }

        handler.handle(CardUpdated(existingCard.id, CardName("updatedName")))

        verify { cardStore.store(existingCard.copy(name = CardName("updatedName"))) }
    }

    @Test
    fun `Should handle only set code updated card event`() {
        every { cardStore.get(existingCard.id) } returns existingCard
        justRun { cardStore.store(existingCard.copy(setCode = CardSetCode("updatedSetCode"))) }

        handler.handle(CardUpdated(existingCard.id, CardSetCode("updatedSetCode")))

        verify { cardStore.store(existingCard.copy(setCode = CardSetCode("updatedSetCode"))) }
    }

    @Test
    fun `Handler should listen to CardCreate`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(CardUpdated::class)
    }
}
