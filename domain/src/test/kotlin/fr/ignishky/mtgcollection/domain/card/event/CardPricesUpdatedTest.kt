package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.mtgcollection.domain.CardFixtures
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated.CardPricesUpdatedHandler
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.model.Price
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
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

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val handler = CardPricesUpdatedHandler(cardProjectionPort)

    @Test
    fun `Should handle prices updated card event`() {
        justRun { cardProjectionPort.update(updatedCard.id, CardPrices(Price(550, 660, 770, 880))) }

        handler.handle(event)

        verify { cardProjectionPort.update(updatedCard.id, CardPrices(Price(550, 660, 770, 880))) }
    }

}
