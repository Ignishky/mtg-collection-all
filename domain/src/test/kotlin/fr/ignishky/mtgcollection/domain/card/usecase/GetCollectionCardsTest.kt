package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetCollectionCardsTest {

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val query = GetCollectionCards(cardProjectionPort)

    @Test
    fun should_return_collection_cards() {
        every { cardProjectionPort.getCollection() } returns listOf(arboreaPegasus, plus2Mace)

        val cards = query.getAll()

        assertThat(cards).containsExactly(arboreaPegasus, plus2Mace)
    }
}
