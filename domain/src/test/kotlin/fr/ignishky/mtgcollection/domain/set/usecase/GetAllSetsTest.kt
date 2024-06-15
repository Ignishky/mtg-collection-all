package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetAllSetsTest {

    private val setProjection = mockk<SetProjectionPort>()

    @Test
    fun should_return_empty_list_when_no_sets_are_stored() {
        every { setProjection.getAll() } returns emptyList()

        val result = GetAllSets(setProjection).getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun should_return_sets_which_has_its_own_icon() {
        every { setProjection.getAll() } returns listOf(
            afr,
            khm.copy(icon = SetIcon("https://scryfall.mtgc.test/sets/fake.svg"))
        )

        val result = GetAllSets(setProjection).getAll()

        assertThat(result).containsOnly(afr)
    }

    @Test
    fun should_return_sets_order_by_released_date() {
        every { setProjection.getAll() } returns listOf(khm, afr)

        val result = GetAllSets(setProjection).getAll()

        assertThat(result).containsExactly(afr, khm)
    }

    @Test
    fun should_return_the_set_corresponding_to_the_code() {
        every { setProjection.get(any<SetCode>()) } returns afr

        val result = GetAllSets(setProjection).get(afr.code)

        assertThat(result).isEqualTo(afr)
    }

}
