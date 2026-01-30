package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedNonFoil
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetAllSetsTest {

    private val setProjection = mockk<SetProjectionPort>()
    private val cardProjection = mockk<CardProjectionPort>()

    private val usecase = GetAllSets(setProjection, cardProjection)

    @Test
    fun should_return_empty_list_when_no_sets_are_stored() {
        every { setProjection.getAll() } returns emptyList()

        val result = usecase.getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun should_return_sets_which_has_its_own_icon() {
        every { setProjection.getAll() } returns listOf(
            afr,
            khm.copy(icon = SetIcon("https://scryfall.mtgc.test/sets/fake.svg"))
        )
        every { cardProjection.getAll(any<SetCode>()) } returns emptyList()

        val result = usecase.getAll()

        assertThat(result).containsOnly(afr.toSetSummary())
    }

    @Test
    fun should_return_sets_order_by_released_date() {
        every { setProjection.getAll() } returns listOf(khm, afr)
        every { cardProjection.getAll(any<SetCode>()) } returns emptyList()

        val result = usecase.getAll()

        assertThat(result).containsExactly(afr.toSetSummary(), khm.toSetSummary())
    }

    @Test
    fun should_return_sets_with_cards_size() {
        every { setProjection.getAll() } returns listOf(afr)
        every { cardProjection.getAll(afr.code) } returns listOf(
            plus2Mace.copy(nbOwnedNonFoil = CardNbOwnedNonFoil(1)),
            arboreaPegasus,
        )

        val result = usecase.getAll()

        assertThat(result).containsOnly(afr.toSetSummary().copy(nbCards = 2, nbOwnedCards = 1))
    }

    @Test
    fun should_return_the_set_corresponding_to_the_code() {
        every { setProjection.get(any<SetCode>()) } returns afr

        val result = usecase.get(afr.code)

        assertThat(result).isEqualTo(afr.toSetSummary())
    }

}
