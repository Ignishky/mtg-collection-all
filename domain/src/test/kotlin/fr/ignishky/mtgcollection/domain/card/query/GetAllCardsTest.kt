package fr.ignishky.mtgcollection.domain.card.query

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.card.exception.NoCardFoundException
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class GetAllCardsTest {

    private val afr = afr()
    private val plus2Mace = plus2Mace()
    private val arboreaPegasus = arboreaPegasus()

    private val cardProjectionPort = mockk<CardProjectionPort>()
    private val query = GetAllCards(cardProjectionPort)

    @Test
    fun `Should throw NoCardFoundException when setCode is unknown`() {
        every { cardProjectionPort.getAll(SetCode("unknown")) } returns emptyList()

        assertThatThrownBy { query.getAll(SetCode("unknown")) }
            .isInstanceOf(NoCardFoundException::class.java)
    }

    @Test
    fun `Should return sorted cards`() {
        every { cardProjectionPort.getAll(afr.code) } returns listOf(arboreaPegasus, plus2Mace)

        val cards = query.getAll(afr.code)

        assertThat(cards).containsExactly(plus2Mace, arboreaPegasus)
    }

}
