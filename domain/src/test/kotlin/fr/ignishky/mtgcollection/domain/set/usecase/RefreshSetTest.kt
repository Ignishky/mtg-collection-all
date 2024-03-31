package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.domain.set.model.SetReleasedAt
import fr.ignishky.mtgcollection.domain.set.model.SetType
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import java.time.LocalDate.now
import kotlin.test.Test

class RefreshSetTest {

    private val setReferer = mockk<SetRefererPort>()
    private val setProjectionPort = mockk<SetProjectionPort>()
    private val handler = RefreshSetHandler(setReferer, setProjectionPort)

    @Nested
    inner class SetFuture {

        @Test
        fun noEventsAreGeneratedWhenSetIsInFuture() {
            projectionReturnEmptyAndRefererReturnSetInTheFuture()

            val events = handler.handle(RefreshSet())

            assertThat(events).isEmpty()
        }

        @Test
        fun noSetsAreCreatedWhenSetIsInFuture() {
            projectionReturnEmptyAndRefererReturnSetInTheFuture()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjectionPort.add(any()) }
        }

        @Test
        fun noSetsAreUpdatedWhenSetIsInFuture() {
            projectionReturnEmptyAndRefererReturnSetInTheFuture()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjectionPort.update(any(), any()) }
        }

        private fun projectionReturnEmptyAndRefererReturnSetInTheFuture() {
            every { setProjectionPort.getAll() } returns emptyList()
            every { setReferer.getAllSets() } returns listOf(afr.copy(releasedAt = SetReleasedAt(now().plusDays(1))))
        }

    }

    @Nested
    inner class SetCreation {

        @Test
        fun setCreatedIsGeneratedWhenRefererSetIsNotStored() {
            projectionReturnEmptyAndRefererReturnNewSet()

            val events = handler.handle(RefreshSet())

            assertThat(events)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
                .containsOnly(SetCreated(afr.id, afr.code, afr.name, afr.type, afr.icon, afr.releasedAt))
        }

        @Test
        fun setIsCreatedWhenRefererSetIsNotStored() {
            projectionReturnEmptyAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify { setProjectionPort.add(afr) }
        }

        @Test
        fun noSetsAreUpdatedWhenARefererSetIsNotStored() {
            projectionReturnEmptyAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjectionPort.update(any(), any()) }
        }

        private fun projectionReturnEmptyAndRefererReturnNewSet() {
            every { setProjectionPort.getAll() } returns emptyList()
            every { setReferer.getAllSets() } returns listOf(afr)
            justRun { setProjectionPort.add(afr) }
        }

    }

    @Nested
    inner class SetNotModify {

        @Test
        fun noEventsAreGeneratedWhenSetIsUnmodified() {
            projectionAndRefererReturnSameList()

            val events = handler.handle(RefreshSet())

            assertThat(events).isEmpty()
        }

        @Test
        fun noSetsAreCreatedWhenSetIsUnmodified() {
            projectionAndRefererReturnSameList()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjectionPort.add(any()) }
        }

        @Test
        fun noSetsAreUpdatedWhenSetIsUnmodified() {
            projectionAndRefererReturnSameList()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjectionPort.update(any(), any()) }
        }

        private fun projectionAndRefererReturnSameList() {
            every { setProjectionPort.getAll() } returns listOf(afr)
            every { setReferer.getAllSets() } returns listOf(afr)
        }

    }

    @Nested
    inner class SetUpdate {

        @Test
        fun setUpdatedIsGeneratedWhenSetIsStoredDifferently() {
            projectionReturnOldSetAndRefererReturnNewSet()

            val events = handler.handle(RefreshSet())

            assertThat(events)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
                .containsOnly(SetUpdated(afr.id, afr.name, afr.type))
        }

        @Test
        fun setIsUpdatedWhenSetIsStoredDifferently() {
            projectionReturnOldSetAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify { setProjectionPort.update(afr.id, listOf(afr.name, afr.type)) }
        }

        @Test
        fun noSetsAreCreatedWhenSetIsStoredDifferently() {
            projectionReturnOldSetAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjectionPort.add(any()) }
        }

        private fun projectionReturnOldSetAndRefererReturnNewSet() {
            every { setProjectionPort.getAll() } returns listOf(afr.copy(name = SetName("Old name"), type = SetType("Old type")))
            every { setReferer.getAllSets() } returns listOf(afr)
            justRun { setProjectionPort.update(afr.id, listOf(afr.name, afr.type)) }
        }

    }

    @Test
    fun shouldListenToRefreshSet() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshSet::class)
    }
}
