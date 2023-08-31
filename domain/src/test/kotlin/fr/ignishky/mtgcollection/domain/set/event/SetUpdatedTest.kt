package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.mtgcollection.domain.fixture.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedHandler
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate.parse
import kotlin.test.Test

class SetUpdatedTest {

    private val existingSet = afr()
    private val updatedSet = existingSet.copy(
        code = SetCode("updatedCode"),
        name = SetName("updatedName"),
        type = SetType("updatedType"),
        icon = SetIcon("updatedIcon"),
        releasedAt = SetReleasedAt(parse("2023-06-12")),
    )
    private val event = SetUpdated(
        existingSet.id,
        SetCode("updatedCode"),
        SetName("updatedName"),
        SetType("updatedType"),
        SetIcon("updatedIcon"),
        SetReleasedAt(parse("2023-06-12")),
    )

    @Test
    fun `Should apply event to set`() {
        val result = event.apply(existingSet)

        assertThat(result).isEqualTo(updatedSet)
    }

    private val setStore = mockk<SetStorePort>()
    private val handler = SetUpdatedHandler(setStore)

    @Test
    fun `Should handle full updated set event`() {
        every { setStore.get(existingSet.id) } returns existingSet
        justRun { setStore.store(updatedSet) }

        handler.handle(event)

        verify { setStore.store(updatedSet) }
    }

    @Test
    fun `Should handle only code updated set event`() {
        every { setStore.get(existingSet.id) } returns existingSet
        justRun { setStore.store(existingSet.copy(code = SetCode("updatedCode"))) }

        handler.handle(SetUpdated(existingSet.id, SetCode("updatedCode")))

        verify { setStore.store(existingSet.copy(code = SetCode("updatedCode"))) }
    }

    @Test
    fun `Should handle only name updated set event`() {
        every { setStore.get(existingSet.id) } returns existingSet
        justRun { setStore.store(existingSet.copy(name = SetName("updatedName"))) }

        handler.handle(SetUpdated(existingSet.id, SetName("updatedName")))

        verify { setStore.store(existingSet.copy(name = SetName("updatedName"))) }
    }

    @Test
    fun `Should handle only type updated set event`() {
        every { setStore.get(existingSet.id) } returns existingSet
        justRun { setStore.store(existingSet.copy(type = SetType("updatedType"))) }

        handler.handle(SetUpdated(existingSet.id, SetType("updatedType")))

        verify { setStore.store(existingSet.copy(type = SetType("updatedType"))) }
    }

    @Test
    fun `Should handle only icon updated set event`() {
        every { setStore.get(existingSet.id) } returns existingSet
        justRun { setStore.store(existingSet.copy(icon = SetIcon("updatedIcon"))) }

        handler.handle(SetUpdated(existingSet.id, SetIcon("updatedIcon")))

        verify { setStore.store(existingSet.copy(icon = SetIcon("updatedIcon"))) }
    }

    @Test
    fun `Should handle only releasedAt updated set event`() {
        every { setStore.get(existingSet.id) } returns existingSet
        justRun { setStore.store(existingSet.copy(releasedAt = SetReleasedAt(parse("2023-06-12")))) }

        handler.handle(SetUpdated(existingSet.id, SetReleasedAt(parse("2023-06-12"))))

        verify { setStore.store(existingSet.copy(releasedAt = SetReleasedAt(parse("2023-06-12")))) }
    }

    @Test
    fun `Handler should listen to SetUpdated`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(SetUpdated::class)
    }
}
