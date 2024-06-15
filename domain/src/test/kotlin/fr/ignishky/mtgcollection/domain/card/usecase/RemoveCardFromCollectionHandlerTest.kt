package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.event.CardDisowned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemoveCardFromCollectionHandlerTest {

    private val cardProjection = mockk<CardProjectionPort>()
    private val handler = RemoveCardFromCollectionHandler(cardProjection)

    @Test
    fun should_do_nothing_for_non_existing_card() {
        every { cardProjection.get(plus2Mace.id) } returns null

        val events = handler.handle(RemoveCardFromCollection(plus2Mace.id))

        assertThat(events).isEmpty()
        verify(exactly = 0) { cardProjection.update(any(), any(), any()) }
    }

    @Test
    fun should_reset_owned_state_to_false() {
        every { cardProjection.get(plus2Mace.id) } returns plus2Mace.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(true))

        val events = handler.handle(RemoveCardFromCollection(plus2Mace.id))

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardDisowned(plus2Mace.id))
        verify { cardProjection.update(plus2Mace.id, CardIsOwned(false), CardIsOwnedFoil(false)) }
    }

    @Test
    fun should_listen_to_RemoveCardFromCollection_command() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RemoveCardFromCollection::class)
    }
}
