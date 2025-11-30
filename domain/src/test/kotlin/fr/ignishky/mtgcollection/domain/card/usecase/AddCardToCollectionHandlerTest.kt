package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwned
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

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(false)))

        assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(any(), any(), any(), any(), any()) }
    }

    @Test
    fun should_return_CardOwned_event_for_new_card() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(
            isOwned = CardIsOwned(false),
            nbOwned = CardNbOwned(0),
            isOwnedFoil = CardIsOwnedFoil(false),
        )

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(false)))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(false)))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(true), CardNbOwned(1), CardIsOwnedFoil(false), CardNbOwnedFoil(0)) }
    }

    @Test
    fun should_return_CardOwned_event_for_a_second_card_addition() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(
            isOwned = CardIsOwned(true),
            nbOwned = CardNbOwned(1),
            isOwnedFoil = CardIsOwnedFoil(false),
        )

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(false)))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(false)))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(true), CardNbOwned(2), CardIsOwnedFoil(false), CardNbOwnedFoil(0)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_new_card() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(
            isOwned = CardIsOwned(false),
            nbOwned = CardNbOwned(0),
            isOwnedFoil = CardIsOwnedFoil(false),
            nbOwnedFoil = CardNbOwnedFoil(0),
        )

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(true)))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(true), CardNbOwned(1), CardIsOwnedFoil(true), CardNbOwnedFoil(1)) }
    }

    @Test
    fun should_return_CardOwned_foil_event_for_a_second_card_addition() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(
            isOwned = CardIsOwned(true),
            nbOwned = CardNbOwned(1),
            isOwnedFoil = CardIsOwnedFoil(true),
            nbOwnedFoil = CardNbOwnedFoil(1),
        )

        val events = handler.handle(AddCardToCollection(plus2Mace.id, CardIsOwnedFoil(true)))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(true), CardNbOwned(2), CardIsOwnedFoil(true), CardNbOwnedFoil(2)) }
    }

    @Test
    fun should_listen_to_AddCardToCollection_command() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(AddCardToCollection::class)
    }
}
