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
import io.mockk.justRun
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshCardTest {

    private val setProjectionPort = mockk<SetProjectionPort>()
    private val cardReferer = mockk<CardRefererPort>()
    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val handler = RefreshCardHandler(setProjectionPort, cardReferer, cardProjectionPort)

    @Test
    fun shouldReturnNoEventsWhenCardsAreUptoDate() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(plus2Mace)
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)

        val events = handler.handle(RefreshCard())

        assertThat(events).isEmpty()
    }

    @Test
    fun shouldReturnCardCreatedEventForNewCard() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns emptyList()
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun { cardProjectionPort.add(plus2Mace) }

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
    }

    @Test
    fun shouldReturnCardUpdatedEventForCardWithNewName() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(plus2Mace.copy(name = CardName("old name")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun { cardProjectionPort.update(plus2Mace.id, listOf(plus2Mace.name)) }

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.name))
    }

    @Test
    fun shouldReturnCardUpdatedEventForCardWithNewSetCode() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(plus2Mace.copy(setCode = CardSetCode("old set code")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun { cardProjectionPort.update(plus2Mace.id, listOf(plus2Mace.setCode)) }

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.setCode))
    }

    @Test
    fun shouldReturnCardUpdatedEventForCardWithNewImages() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(plus2Mace.copy(images = CardImages(listOf(CardImage("old image")))))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun { cardProjectionPort.update(plus2Mace.id, listOf(plus2Mace.images)) }

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.images))
    }

    @Test
    fun shouldReturnCardUpdatedEventForCardWithNewCollectionNumber() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(plus2Mace.copy(collectionNumber = CardNumber("old collection number")))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun { cardProjectionPort.update(plus2Mace.id, listOf(plus2Mace.collectionNumber)) }

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardUpdated(plus2Mace.id, plus2Mace.collectionNumber))
    }

    @Test
    fun shouldReturnCardPricesUpdatedEventForCardWithOnlyNewPrices() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(plus2Mace.copy(prices = CardPrices(Price(110, 220, 330, 440))))
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun { cardProjectionPort.update(plus2Mace.id, plus2Mace.prices) }

        val events = handler.handle(RefreshCard())

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardPricesUpdated(plus2Mace.id, plus2Mace.prices))
    }

    @Test
    fun shouldReturnEventsForCardWithAllNewFieldValues() {
        every { setProjectionPort.getAll() } returns listOf(afr)
        every { cardProjectionPort.getAll(afr.code) } returns listOf(
            plus2Mace.copy(
                name = CardName("old name"),
                setCode = CardSetCode("old set code"),
                images = CardImages(listOf(CardImage("old image"))),
                collectionNumber = CardNumber("old collection number"),
                prices = CardPrices(Price(110, 220, 330, 440)),
            )
        )
        every { cardReferer.getCards(afr.code) } returns listOf(plus2Mace)
        justRun {
            cardProjectionPort.update(
                plus2Mace.id, listOf(
                    plus2Mace.collectionNumber,
                    plus2Mace.images,
                    plus2Mace.name,
                    plus2Mace.setCode
                )
            )
        }
        justRun { cardProjectionPort.update(plus2Mace.id, plus2Mace.prices) }

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
    }

    @Test
    fun shouldListenToRefreshCard() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshCard::class)
    }
}
