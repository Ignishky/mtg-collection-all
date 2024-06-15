package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetCollectionCardsTest {

    private val cardProjection = mockk<CardProjectionPort>()
    private val query = GetCollectionCards(cardProjection)

    @Test
    fun should_return_collection_cards() {
        every { cardProjection.getCollection() } returns listOf(arboreaPegasus, plus2Mace)

        val cards = query.getAll()

        assertThat(cards).containsExactly(arboreaPegasus, plus2Mace)
    }
}
