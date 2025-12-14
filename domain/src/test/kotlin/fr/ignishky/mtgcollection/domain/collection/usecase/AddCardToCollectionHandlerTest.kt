package fr.ignishky.mtgcollection.domain.collection.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures
import fr.ignishky.mtgcollection.domain.collection.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedNonFoil
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AddCardToCollectionHandlerTest {

    private val cardProjection = mockk<CardProjectionPort>()
    private val handler = AddCardToCollectionHandler(cardProjection)

    @Test
    fun should_do_nothing_for_non_existing_card() {
        every { cardProjection.get(CardFixtures.plus2Mace.id) } returns null

        val events = handler.handle(AddCardToCollection(CardFixtures.plus2Mace.id, false))

        Assertions.assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(any(), any(), any()) }
    }

    @Test
    fun should_return_CardOwned_event_for_new_non_foil_card() {
        every { cardProjection.get(CardFixtures.plus2Mace.id) } returns CardFixtures.plus2Mace

        val events = handler.handle(AddCardToCollection(CardFixtures.plus2Mace.id, false))

        Assertions.assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(CardFixtures.plus2Mace.id, false))
        verify { cardProjection.update(CardFixtures.plus2Mace.id, CardNbOwnedNonFoil(1), CardNbOwnedFoil(0)) }
    }

    @Test
    fun should_return_CardOwned_event_for_a_second_card_addition() {
        every { cardProjection.get(CardFixtures.plus2Mace.id) } returns CardFixtures.plus2Mace.copy(
            nbOwnedNonFoil = CardNbOwnedNonFoil(1),
        )

        val events = handler.handle(AddCardToCollection(CardFixtures.plus2Mace.id, false))

        Assertions.assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(CardFixtures.plus2Mace.id, false))
        verify { cardProjection.update(CardFixtures.plus2Mace.id, CardNbOwnedNonFoil(2), CardNbOwnedFoil(0)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_new_card() {
        every { cardProjection.get(CardFixtures.plus2Mace.id) } returns CardFixtures.plus2Mace

        val events = handler.handle(AddCardToCollection(CardFixtures.plus2Mace.id, true))

        Assertions.assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(CardFixtures.plus2Mace.id, true))
        verify { cardProjection.update(CardFixtures.plus2Mace.id, CardNbOwnedNonFoil(0), CardNbOwnedFoil(1)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_a_second_card_addition() {
        every { cardProjection.get(CardFixtures.plus2Mace.id) } returns CardFixtures.plus2Mace.copy(
            nbOwnedFoil = CardNbOwnedFoil(1),
        )

        val events = handler.handle(AddCardToCollection(CardFixtures.plus2Mace.id, true))

        Assertions.assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(CardFixtures.plus2Mace.id, true))
        verify { cardProjection.update(CardFixtures.plus2Mace.id, CardNbOwnedNonFoil(0), CardNbOwnedFoil(2)) }
    }

    @Test
    fun should_listen_to_AddCardToCollection_command() {
        val listenTo = handler.listenTo()

        Assertions.assertThat(listenTo).isEqualTo(AddCardToCollection::class)
    }
}