package fr.ignishky.mtgcollection.domain.card.command

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.card.command.RefreshCard.RefreshCardHandler
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshCardTest {

    private val correlationId = CorrelationId("a-correlation-id")
    private val afr = afr()
    private val plus2Mace = plus2Mace()

    private val setStore = mockk<SetStorePort>()
    private val cardReferer = mockk<CardRefererPort>()
    private val cardStore = mockk<CardStorePort>()
    private val handler = RefreshCardHandler(setStore, cardReferer, cardStore)

    @Test
    fun `Should return no events when cards are up to date`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events).isEmpty()
    }

    @Test
    fun `Should return CardCreated event for new card`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns emptyList()
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardCreated(
                    correlationId,
                    plus2Mace.id,
                    plus2Mace.name,
                    plus2Mace.setCode,
                    plus2Mace.prices,
                    plus2Mace.images,
                    plus2Mace.collectionNumber,
                )
            )
    }

    @Test
    fun `Should return CardUpdated event for card with new name`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace.copy(name = CardName("old name")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(correlationId, plus2Mace.id, plus2Mace.name))
    }

    @Test
    fun `Should return CardUpdated event for card with new setCode`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace.copy(setCode = CardSetCode("old set code")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(correlationId, plus2Mace.id, plus2Mace.setCode))
    }

    @Test
    fun `Should return CardUpdated event for card with new images`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace.copy(images = CardImages(listOf(CardImage("new image")))))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(correlationId, plus2Mace.id, plus2Mace.images))
    }

    @Test
    fun `Should return CardUpdated event for card with new collection number`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace.copy(collectionNumber = CardNumber("new collection number")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(correlationId, plus2Mace.id, plus2Mace.collectionNumber))
    }

    @Test
    fun `Should return CardPricesUpdated event for card with only new prices`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace.copy(prices = CardPrices(Price(110, 220, 330, 440))))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardPricesUpdated(correlationId, plus2Mace.id, plus2Mace.prices))
    }

    @Test
    fun `Should return events for card with all new field values`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(
            plus2Mace.copy(
                name = CardName("old name"),
                setCode = CardSetCode("old set code"),
                images = CardImages(listOf(CardImage("new image"))),
                collectionNumber = CardNumber("new collection number"),
                prices = CardPrices(Price(110, 220, 330, 440)),
            )
        )
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(
                    correlationId,
                    plus2Mace.id,
                    plus2Mace.name,
                    plus2Mace.setCode,
                    plus2Mace.images,
                    plus2Mace.collectionNumber,
                ),
                CardPricesUpdated(
                    correlationId,
                    plus2Mace.id,
                    plus2Mace.prices,
                )
            )
    }

    @Test
    fun `Handler should listen to RefreshCard`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshCard::class)
    }
}
