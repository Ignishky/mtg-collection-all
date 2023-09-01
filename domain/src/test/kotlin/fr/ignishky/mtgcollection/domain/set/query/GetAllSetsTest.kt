package fr.ignishky.mtgcollection.domain.set.query

import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetAllSetsTest {

    private val setStore = mockk<SetStorePort>()

    @Test
    fun `Should return empty list when no sets are stored`() {
        every { setStore.getAll() } returns listOf()

        val result = GetAllSets(setStore).getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `Should return sets which has its own icon`() {
        every { setStore.getAll() } returns listOf(
            afr(),
            khm().copy(icon = SetIcon("https://scryfall.mtgc.test/sets/fake.svg"))
        )

        val result = GetAllSets(setStore).getAll()

        assertThat(result).containsOnly(afr())
    }

    @Test
    fun `Should return sets order by release date`() {
        every { setStore.getAll() } returns listOf(khm(), afr())

        val result = GetAllSets(setStore).getAll()

        assertThat(result).containsExactly(afr(), khm())
    }

}
