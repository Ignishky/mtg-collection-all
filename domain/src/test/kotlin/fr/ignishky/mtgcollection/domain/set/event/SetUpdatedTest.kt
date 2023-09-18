package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedHandler
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate.parse

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
        CorrelationId("SetUpdated_CorrelationId"),
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
        justRun {
            setStore.update(
                updatedSet.id, listOf(
                    SetCode("updatedCode"),
                    SetName("updatedName"),
                    SetType("updatedType"),
                    SetIcon("updatedIcon"),
                    SetReleasedAt(parse("2023-06-12"))
                )
            )
        }

        handler.handle(event)

        verify {
            setStore.update(
                updatedSet.id, listOf(
                    SetCode("updatedCode"),
                    SetName("updatedName"),
                    SetType("updatedType"),
                    SetIcon("updatedIcon"),
                    SetReleasedAt(parse("2023-06-12"))
                )
            )
        }
    }

    @Test
    fun `Should handle only code updated set event`() {
        justRun { setStore.update(existingSet.id, listOf(SetCode("updatedCode"))) }

        handler.handle(SetUpdated(CorrelationId("SetUpdated_CorrelationId"), existingSet.id, SetCode("updatedCode")))

        verify { setStore.update(existingSet.id, listOf(SetCode("updatedCode"))) }
    }

    @Test
    fun `Should handle only name updated set event`() {
        justRun { setStore.update(existingSet.id, listOf(SetName("updatedName"))) }

        handler.handle(SetUpdated(CorrelationId("SetUpdated_CorrelationId"), existingSet.id, SetName("updatedName")))

        verify { setStore.update(existingSet.id, listOf(SetName("updatedName"))) }
    }

    @Test
    fun `Should handle only type updated set event`() {
        justRun { setStore.update(existingSet.id, listOf(SetType("updatedType"))) }

        handler.handle(SetUpdated(CorrelationId("SetUpdated_CorrelationId"), existingSet.id, SetType("updatedType")))

        verify { setStore.update(existingSet.id, listOf(SetType("updatedType"))) }
    }

    @Test
    fun `Should handle only icon updated set event`() {
        justRun { setStore.update(existingSet.id, listOf(SetIcon("updatedIcon"))) }

        handler.handle(SetUpdated(CorrelationId("SetUpdated_CorrelationId"), existingSet.id, SetIcon("updatedIcon")))

        verify { setStore.update(existingSet.id, listOf(SetIcon("updatedIcon"))) }
    }

    @Test
    fun `Should handle only releasedAt updated set event`() {
        justRun { setStore.update(existingSet.id, listOf(SetReleasedAt(parse("2023-06-12")))) }

        handler.handle(SetUpdated(CorrelationId("SetUpdated_CorrelationId"), existingSet.id, SetReleasedAt(parse("2023-06-12"))))

        verify { setStore.update(existingSet.id, listOf(SetReleasedAt(parse("2023-06-12")))) }
    }

    @Test
    fun `Handler should listen to SetUpdated`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(SetUpdated::class)
    }
}
