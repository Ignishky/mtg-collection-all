package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetAllSetsTest {

    private val setProjectionPort = mockk<SetProjectionPort>()

    @Test
    fun `Should return empty list when no sets are stored`() {
        every { setProjectionPort.getAll() } returns emptyList()

        val result = GetAllSets(setProjectionPort).getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `Should return sets which has its own icon`() {
        every { setProjectionPort.getAll() } returns listOf(
            afr(),
            khm().copy(icon = SetIcon("https://scryfall.mtgc.test/sets/fake.svg"))
        )

        val result = GetAllSets(setProjectionPort).getAll()

        assertThat(result).containsOnly(afr())
    }

    @Test
    fun `Should return sets order by release date`() {
        every { setProjectionPort.getAll() } returns listOf(khm(), afr())

        val result = GetAllSets(setProjectionPort).getAll()

        assertThat(result).containsExactly(afr(), khm())
    }

}
