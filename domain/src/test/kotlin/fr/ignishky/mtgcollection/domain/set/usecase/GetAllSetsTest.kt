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

    private val setProjectionPort = mockk<SetProjectionPort>()

    @Test
    fun should_return_empty_list_when_no_sets_are_stored() {
        every { setProjectionPort.getAll() } returns emptyList()

        val result = GetAllSets(setProjectionPort).getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun should_return_sets_which_has_its_own_icon() {
        every { setProjectionPort.getAll() } returns listOf(
            afr,
            khm.copy(icon = SetIcon("https://scryfall.mtgc.test/sets/fake.svg"))
        )

        val result = GetAllSets(setProjectionPort).getAll()

        assertThat(result).containsOnly(afr)
    }

    @Test
    fun should_return_sets_order_by_released_date() {
        every { setProjectionPort.getAll() } returns listOf(khm, afr)

        val result = GetAllSets(setProjectionPort).getAll()

        assertThat(result).containsExactly(afr, khm)
    }

    @Test
    fun should_return_the_set_corresponding_to_the_code() {
        every { setProjectionPort.get(any<SetCode>()) } returns afr

        val result = GetAllSets(setProjectionPort).get(afr.code)

        assertThat(result).isEqualTo(afr)
    }

}
