package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.CardFixtures.valorSinger
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshCardHandlerTest {

    private val cardReferer = mockk<CardRefererPort>()
    private val cardProjection = mockk<CardProjectionPort>()
    private val handler = RefreshCardHandler(cardReferer, cardProjection)

    @Test
    fun should_return_no_events_when_cards_are_upToDate() {
        every { cardProjection.getAll() } returns listOf(plus2Mace)
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
    }

    @Test
    fun should_return_no_events_when_card_creation_throws_exception() {
        every { cardProjection.getAll() } returns emptyList()
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)
        every { cardProjection.add(any()) } throws IllegalStateException("Test Exception")
        every { cardProjection.get(plus2Mace.id) } returns null

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
    }

    @Test
    fun should_return_CardCreated_event_for_new_card() {
        every { cardProjection.getAll() } returns emptyList()
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardCreated(
                    plus2Mace.id,
                    plus2Mace.name,
                    plus2Mace.setCode,
                    plus2Mace.prices,
                    plus2Mace.images,
                    plus2Mace.collectionNumber,
                    plus2Mace.finishes,
                    plus2Mace.colors,
                )
            )
        verify { cardProjection.add(plus2Mace) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_name() {
        every { cardProjection.getAll() } returns listOf(plus2Mace.copy(name = CardName("old name")))
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.name))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.name)) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_setCode() {
        every { cardProjection.getAll() } returns listOf(plus2Mace.copy(setCode = CardSetCode("old set code")))
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.setCode))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.setCode)) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_images() {
        every { cardProjection.getAll() } returns listOf(plus2Mace.copy(images = CardImages(listOf(CardImage("old image")))))
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.images))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.images)) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_collection_number() {
        every { cardProjection.getAll() } returns listOf(plus2Mace.copy(collectionNumber = CardNumber("old collection number")))
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.collectionNumber))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.collectionNumber)) }
    }

    @Test
    fun should_ignore_prices_update_when_new_card_has_no_price() {
        every { cardProjection.getAll() } returns listOf(plus2Mace)
        every { cardReferer.getAllCards() } returns listOf(plus2Mace.copy(prices = CardPrices(Price(0, 0, 0, 0))))

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(plus2Mace.id, plus2Mace.prices) }
    }

    @Test
    fun should_return_CardPricesUpdated_event_for_card_with_only_new_prices() {
        every { cardProjection.getAll() } returns listOf(plus2Mace.copy(prices = CardPrices(Price(110, 220, 330, 440))))
        every { cardReferer.getAllCards() } returns listOf(plus2Mace.copy(prices = CardPrices(Price(0, 250, 0, 450))))

        val events = handler.handle(RefreshCard())

        val updatedCardPrices = CardPrices(Price(110, 250, 330, 450))
        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardPricesUpdated(plus2Mace.id, updatedCardPrices))
        verify { cardProjection.update(plus2Mace.id, updatedCardPrices) }
    }

    @Test
    fun should_return_events_for_card_with_all_new_field_values() {
        every { cardProjection.getAll() } returns listOf(
            plus2Mace.copy(
                name = CardName("old name"),
                setCode = CardSetCode("old set code"),
                images = CardImages(listOf(CardImage("old image"))),
                collectionNumber = CardNumber("old collection number"),
                prices = CardPrices(Price(110, 0, 330, 0)),
            )
        )
        every { cardReferer.getAllCards() } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(
                    plus2Mace.id,
                    plus2Mace.name,
                    plus2Mace.setCode,
                    plus2Mace.images,
                    plus2Mace.collectionNumber,
                ),
                CardPricesUpdated(
                    plus2Mace.id,
                    plus2Mace.prices,
                )
            )
        verify {
            cardProjection.update(
                plus2Mace.id, listOf(
                    plus2Mace.collectionNumber,
                    plus2Mace.images,
                    plus2Mace.name,
                    plus2Mace.setCode
                )
            )
        }
        verify { cardProjection.update(plus2Mace.id, plus2Mace.prices) }
    }

    @Test
    fun should_return_events_for_card_with_different_setCode() {
        every { cardProjection.getAll() } returns listOf(plus2Mace)
        every { cardReferer.getAllCards() } returns listOf(plus2Mace, valorSinger)
        every { cardProjection.add(valorSinger) } throws IllegalStateException("Test Exception")
        every { cardProjection.get(valorSinger.id) } returns valorSinger.copy(
            setCode = CardSetCode("old set code"),
            prices = CardPrices(Price(110, 0, 330, 0)),
        )

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(
                    valorSinger.id,
                    valorSinger.setCode,
                ),
                CardPricesUpdated(
                    valorSinger.id,
                    valorSinger.prices,
                )
            )
        verify { cardProjection.update(valorSinger.id, listOf(valorSinger.setCode)) }
        verify { cardProjection.update(valorSinger.id, valorSinger.prices) }
    }

    @Test
    fun should_listen_to_RefreshCard_command() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshCard::class)
    }
}
