package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddCardToCollectionHandlerTest {

    private val cardProjection = mockk<CardProjectionPort>()
    private val handler = AddCardToCollectionHandler(cardProjection)

    @Test
    fun should_do_nothing_for_non_existing_card() {
        every { cardProjection.get(plus2Mace.id) } returns null

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(false)))

        assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(any(), any(), any()) }
    }

    @Test
    fun should_return_CardOwned_event_for_existing_card() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(false)))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(false)))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(true), CardIsOwnedFoil(false)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_existing_card() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(true)))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(true), CardIsOwnedFoil(true)) }
    }

    @Test
    fun should_listen_to_AddCardToCollection_command() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(AddCardToCollection::class)
    }
}
