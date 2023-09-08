package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.mtgcollection.domain.CardFixtures
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated.CardPricesUpdatedHandler
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.model.Price
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CardPricesUpdatedTest {

    private val existingCard = CardFixtures.plus2Mace()
    private val event = CardPricesUpdated(
        existingCard.id,
        CardPrices(Price(550, 660, 770, 880)),
    )
    private val updatedCard = existingCard.copy(
        prices = CardPrices(Price(550, 660, 770, 880)),
    )

    @Test
    fun `Should apply event to card`() {
        val result = event.apply(existingCard)

        assertThat(result).isEqualTo(updatedCard)
    }

    private val cardStore = mockk<CardStorePort>()
    private val handler = CardPricesUpdatedHandler(cardStore)

    @Test
    fun `Should handle prices updated card event`() {
        every { cardStore.get(existingCard.id) } returns existingCard
        justRun { cardStore.store(updatedCard) }

        handler.handle(event)

        verify { cardStore.store(updatedCard) }
    }

}
