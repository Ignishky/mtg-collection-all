package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedHandler
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
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

    private val setProjectionPort = mockk<SetProjectionPort>()
    private val handler = SetUpdatedHandler(setProjectionPort)

    @Test
    fun `Should handle full updated set event`() {
        justRun {
            setProjectionPort.update(
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
            setProjectionPort.update(
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

    companion object {
        @JvmStatic
        fun setPropertyProvider(): List<SetProperty> {
            return listOf(
                SetCode("updatedCode"),
                SetName("updatedName"),
                SetType("updatedType"),
                SetIcon("updatedIcon"),
                SetReleasedAt(parse("2023-06-12")),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("setPropertyProvider")
    fun `Should handle updated set event`(property: SetProperty) {
        justRun { setProjectionPort.update(existingSet.id, listOf(property)) }

        handler.handle(SetUpdated(existingSet.id, property))

        verify { setProjectionPort.update(existingSet.id, listOf(property)) }
    }

    @Test
    fun `Handler should listen to SetUpdated`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(SetUpdated::class)
    }

}
