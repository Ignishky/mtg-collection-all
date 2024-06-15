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
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import java.time.LocalDate.now
import kotlin.test.Test

class RefreshSetTest {

    private val setReferer = mockk<SetRefererPort>()
    private val setProjection = mockk<SetProjectionPort>()
    private val handler = RefreshSetHandler(setReferer, setProjection)

    @Nested
    inner class SetFuture {

        @Test
        fun no_events_are_generated_when_set_is_in_future() {
            projectionReturnEmptyAndRefererReturnSetInTheFuture()

            val events = handler.handle(RefreshSet())

            assertThat(events).isEmpty()
        }

        @Test
        fun no_sets_are_created_when_set_is_in_future() {
            projectionReturnEmptyAndRefererReturnSetInTheFuture()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjection.add(any()) }
        }

        @Test
        fun no_sets_are_updated_when_set_is_in_future() {
            projectionReturnEmptyAndRefererReturnSetInTheFuture()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjection.update(any(), any()) }
        }

        private fun projectionReturnEmptyAndRefererReturnSetInTheFuture() {
            every { setProjection.getAll() } returns emptyList()
            every { setReferer.getAllSets() } returns listOf(afr.copy(releasedAt = SetReleasedAt(now().plusDays(1))))
        }

    }

    @Nested
    inner class SetCreation {

        @Test
        fun set_created_is_generated_when_referer_set_is_not_stored() {
            projectionReturnEmptyAndRefererReturnNewSet()

            val events = handler.handle(RefreshSet())

            assertThat(events)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
                .containsOnly(SetCreated(afr.id, afr.code, afr.name, afr.type, afr.icon, afr.releasedAt))
            verify { setProjection.add(afr) }
        }

        @Test
        fun set_is_created_when_referer_set_is_not_stored() {
            projectionReturnEmptyAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify { setProjection.add(afr) }
            verify { setProjection.add(afr) }
        }

        @Test
        fun no_sets_are_updated_when_a_referer_set_is_not_stored() {
            projectionReturnEmptyAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjection.update(any(), any()) }
            verify { setProjection.add(afr) }
        }

        private fun projectionReturnEmptyAndRefererReturnNewSet() {
            every { setProjection.getAll() } returns emptyList()
            every { setReferer.getAllSets() } returns listOf(afr)
        }

    }

    @Nested
    inner class SetNotModify {

        @Test
        fun no_events_are_generated_when_set_is_unmodified() {
            projectionAndRefererReturnSameList()

            val events = handler.handle(RefreshSet())

            assertThat(events).isEmpty()
        }

        @Test
        fun no_sets_are_created_when_set_is_unmodified() {
            projectionAndRefererReturnSameList()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjection.add(any()) }
        }

        @Test
        fun no_sets_are_updated_when_set_is_unmodified() {
            projectionAndRefererReturnSameList()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjection.update(any(), any()) }
        }

        private fun projectionAndRefererReturnSameList() {
            every { setProjection.getAll() } returns listOf(afr)
            every { setReferer.getAllSets() } returns listOf(afr)
        }

    }

    @Nested
    inner class SetUpdate {

        @Test
        fun set_updated_is_generated_when_set_is_stored_differently() {
            projectionReturnOldSetAndRefererReturnNewSet()

            val events = handler.handle(RefreshSet())

            assertThat(events)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
                .containsOnly(SetUpdated(afr.id, afr.name, afr.type))
            verify { setProjection.update(afr.id, listOf(afr.name, afr.type)) }
        }

        @Test
        fun set_is_updated_when_set_is_stored_differently() {
            projectionReturnOldSetAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify { setProjection.update(afr.id, listOf(afr.name, afr.type)) }
            verify { setProjection.update(afr.id, listOf(afr.name, afr.type)) }
        }

        @Test
        fun no_sets_are_created_when_set_is_stored_differently() {
            projectionReturnOldSetAndRefererReturnNewSet()

            handler.handle(RefreshSet())

            verify(exactly = 0) { setProjection.add(any()) }
            verify { setProjection.update(afr.id, listOf(afr.name, afr.type)) }
        }

        private fun projectionReturnOldSetAndRefererReturnNewSet() {
            every { setProjection.getAll() } returns listOf(afr.copy(name = SetName("Old name"), type = SetType("Old type")))
            every { setReferer.getAllSets() } returns listOf(afr)
        }

    }

    @Test
    fun should_listen_to_refreshSet() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshSet::class)
    }
}
