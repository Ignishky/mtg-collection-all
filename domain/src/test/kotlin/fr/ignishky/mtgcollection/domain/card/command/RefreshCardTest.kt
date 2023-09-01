package fr.ignishky.mtgcollection.domain.card.command

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.command.RefreshCard.RefreshCardHandler
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
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
        every { cardStore.get(afr.code) } returns listOf()
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardCreated(plus2Mace.id, plus2Mace.name, plus2Mace.setCode, plus2Mace.prices, plus2Mace.images, plus2Mace.collectionNumber)
            )
    }

    @Test
    fun `Should return CardUpdated event for card with new name`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace.copy(name = CardName("new name")))

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(plus2Mace.id, CardName("new name"), plus2Mace.prices, plus2Mace.images, plus2Mace.collectionNumber)
            )
    }

    @Test
    fun `Should return CardUpdated event for card with new prices`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace.copy(prices = CardPrices(Price(110, 220, 330, 440))))

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(plus2Mace.id, plus2Mace.name, CardPrices(Price(110, 220, 330, 440)), plus2Mace.images, plus2Mace.collectionNumber)
            )
    }

    @Test
    fun `Should return CardUpdated event for card with new images`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace.copy(images = CardImages(CardImage("new image"))))

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(plus2Mace.id, plus2Mace.name, plus2Mace.prices, CardImages(CardImage("new image")), plus2Mace.collectionNumber)
            )
    }

    @Test
    fun `Should return CardUpdated event for card with new collection number`() {
        every { setStore.getAll() } returns listOf(afr)
        every { cardStore.get(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace.copy(collectionNumber = CardNumber("new collection number")))

        val events = handler.handle(RefreshCard(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                CardUpdated(plus2Mace.id, plus2Mace.name, plus2Mace.prices, plus2Mace.images, CardNumber("new collection number"))
            )
    }

    @Test
    fun `Handler should listen to RefreshCard`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshCard::class)
    }
}
