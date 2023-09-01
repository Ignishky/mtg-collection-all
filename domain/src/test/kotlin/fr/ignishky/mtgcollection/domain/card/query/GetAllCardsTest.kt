package fr.ignishky.mtgcollection.domain.card.query

import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetAllCardsTest {

    private val afr = afr()
    private val plus2Mace = plus2Mace()
    private val arboreaPegasus = arboreaPegasus()

    private val cardStore = mockk<CardStorePort>()
    private val query = GetAllCards(cardStore)

    @Test
    fun `Should return sorted cards`() {
        every { cardStore.get(afr.code) } returns listOf(arboreaPegasus, plus2Mace)

        val cards = query.getAll(afr.code)

        assertThat(cards).containsExactly(plus2Mace, arboreaPegasus)
    }
}
