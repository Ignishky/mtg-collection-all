package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedHandler
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate.parse

class SetUpdatedTest {

    private val updatedSet = afr.copy(
        code = SetCode("updatedCode"),
        name = SetName("updatedName"),
        type = SetType("updatedType"),
        icon = SetIcon("updatedIcon"),
        releasedAt = SetReleasedAt(parse("2023-06-12")),
    )
    private val event = SetUpdated(
        afr.id,
        updatedSet.code,
        updatedSet.name,
        updatedSet.type,
        updatedSet.icon,
        updatedSet.releasedAt,
    )

    @Test
    fun `Should apply event to set`() {
        val result = event.apply(afr)

        assertThat(result).isEqualTo(updatedSet)
    }

    @Nested
    inner class HandlerTest {

        private val setProjectionPort = mockk<SetProjectionPort>()
        private val handler = SetUpdatedHandler(setProjectionPort)

        @Test
        fun `Should handle full updated set event`() {
            justRun { setProjectionPort.update(updatedSet.id, event.payload.toProperties()) }

            handler.handle(event)

            verify { setProjectionPort.update(updatedSet.id, event.payload.toProperties()) }
        }

        @Test
        fun `Handler should listen to SetUpdated`() {
            val listenTo = handler.listenTo()

            assertThat(listenTo).isEqualTo(SetUpdated::class)
        }
    }
}
