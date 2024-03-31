package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetAllCardsTest {

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val query = GetAllCards(cardProjectionPort)

    @Test
    fun shouldReturnSortedCards() {
        every { cardProjectionPort.getAll(afr.code) } returns listOf(arboreaPegasus, plus2Mace)

        val cards = query.getAll(afr.code)

        assertThat(cards).containsExactly(plus2Mace, arboreaPegasus)
    }

}
