package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedNonFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedFoil
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

        val events = handler.handle(AddCardToCollection(plus2Mace.id, false))

        assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(any(), any(), any()) }
    }

    @Test
    fun should_return_CardOwned_event_for_new_non_foil_card() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace

        val events = handler.handle(AddCardToCollection(plus2Mace.id, false))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, false))
        verify { cardProjection.update(plus2Mace.id, CardNbOwnedNonFoil(1), CardNbOwnedFoil(0)) }
    }

    @Test
    fun should_return_CardOwned_event_for_a_second_card_addition() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(
            nbOwnedNonFoil = CardNbOwnedNonFoil(1),
        )

        val events = handler.handle(AddCardToCollection(plus2Mace.id, false))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, false))
        verify { cardProjection.update(plus2Mace.id, CardNbOwnedNonFoil(2), CardNbOwnedFoil(0)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_new_card() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace

        val events = handler.handle(AddCardToCollection(plus2Mace.id, true))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, true))
        verify { cardProjection.update(plus2Mace.id, CardNbOwnedNonFoil(0), CardNbOwnedFoil(1)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_a_second_card_addition() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(
            nbOwnedFoil = CardNbOwnedFoil(1),
        )

        val events = handler.handle(AddCardToCollection(plus2Mace.id, true))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, true))
        verify { cardProjection.update(plus2Mace.id, CardNbOwnedNonFoil(0), CardNbOwnedFoil(2)) }
    }

    @Test
    fun should_listen_to_AddCardToCollection_command() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(AddCardToCollection::class)
    }
}
