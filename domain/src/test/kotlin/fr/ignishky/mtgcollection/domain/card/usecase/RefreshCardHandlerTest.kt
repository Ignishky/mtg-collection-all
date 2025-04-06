package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshCardHandlerTest {

    private val setProjection = mockk<SetProjectionPort>()
    private val cardReferer = mockk<CardRefererPort>()
    private val cardProjection = mockk<CardProjectionPort>()
    private val handler = RefreshCardHandler(setProjection, cardReferer, cardProjection)

    @Test
    fun should_return_no_events_when_cards_are_uptoDate() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
    }

    @Test
    fun should_return_no_events_when_card_creation_throws_exception() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns emptyList()
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        every { cardProjection.add(any()) } throws IllegalStateException("Test Exception")

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
    }

    @Test
    fun should_return_CardCreated_event_for_new_card() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns emptyList()
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

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
                )
            )
        verify { cardProjection.add(plus2Mace) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_name() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace.copy(name = CardName("old name")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.name))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.name)) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_setCode() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace.copy(setCode = CardSetCode("old set code")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.setCode))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.setCode)) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_images() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace.copy(images = CardImages(listOf(CardImage("old image")))))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.images))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.images)) }
    }

    @Test
    fun should_return_CardUpdated_event_for_card_with_new_collection_number() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace.copy(collectionNumber = CardNumber("old collection number")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.collectionNumber))
        verify { cardProjection.update(plus2Mace.id, listOf(plus2Mace.collectionNumber)) }
    }

    @Test
    fun should_ignore_prices_update_when_new_card_has_no_price() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace.copy(prices = CardPrices(Price(0, 0, 0, 0))))

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(plus2Mace.id, plus2Mace.prices) }
    }

    @Test
    fun should_return_CardPricesUpdated_event_for_card_with_only_new_prices() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(plus2Mace.copy(prices = CardPrices(Price(110, 220, 330, 440))))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace.copy(prices = CardPrices(Price(0, 250, 0, 450))))

        val events = handler.handle(RefreshCard())

        val updatedCardPrices = CardPrices(Price(110, 250, 330, 450))
        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardPricesUpdated(plus2Mace.id, updatedCardPrices))
        verify { cardProjection.update(plus2Mace.id, updatedCardPrices) }
    }

    @Test
    fun should_return_events_for_card_with_all_new_field_values() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(
            plus2Mace.copy(
                name = CardName("old name"),
                setCode = CardSetCode("old set code"),
                images = CardImages(listOf(CardImage("old image"))),
                collectionNumber = CardNumber("old collection number"),
                prices = CardPrices(Price(110, 0, 330, 0)),
            )
        )
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

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
    fun should_listen_to_RefreshCard_command() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshCard::class)
    }
}
