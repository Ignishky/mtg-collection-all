package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.mtgcollection.domain.set.event.SetCreated.SetCreatedHandler
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate.parse

class SetCreatedTest {

    private val emptySet = Set()
    private val newSet = emptySet.copy(
        id = SetId("createdId"),
        code = SetCode("createdCode"),
        name = SetName("createdName"),
        type = SetType("createdType"),
        icon = SetIcon("createdIcon"),
        releasedAt = SetReleasedAt(parse("2023-06-04")),
    )
    private val event = SetCreated(newSet.id, newSet.code, newSet.name, newSet.type, newSet.icon, newSet.releasedAt)

    @Test
    fun `Should apply event to set`() {
        val result = event.apply(emptySet)

        assertThat(result).isEqualTo(newSet)
    }

    @Nested
    inner class HandlerTest {

        private val setProjectionPort = mockk<SetProjectionPort>()
        private val handler = SetCreatedHandler(setProjectionPort)

        @Test
        fun `Should handle created event`() {
            justRun { setProjectionPort.add(newSet) }

            handler.handle(event)

            verify { setProjectionPort.add(newSet) }
        }

        @Test
        fun `Handler should listen to SetCreated`() {
            val listenTo = handler.listenTo()

            assertThat(listenTo).isEqualTo(SetCreated::class)
        }
    }
}
