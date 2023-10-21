package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.mtgcollection.domain.set.event.SetCreated.SetCreatedHandler
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import io.mockk.justRun
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate.parse

class SetCreatedTest {

    private val emptySet = Set()
    private val event = SetCreated(
        SetId("createdId"),
        SetCode("createdCode"),
        SetName("createdName"),
        SetType("createdType"),
        SetIcon("createdIcon"),
        SetReleasedAt(parse("2023-06-12")),
    )

    @Test
    fun `Should apply event to set`() {
        val result = event.apply(emptySet)

        assertThat(result).isEqualTo(
            emptySet.copy(
                id = SetId("createdId"),
                code = SetCode("createdCode"),
                name = SetName("createdName"),
                type = SetType("createdType"),
                icon = SetIcon("createdIcon"),
                releasedAt = SetReleasedAt(parse("2023-06-12")),
            )
        )
    }

    private val setProjectionPort = mockk<SetProjectionPort>()
    private val handler = SetCreatedHandler(setProjectionPort)

    @Test
    fun `Should handle created event`() {
        justRun {
            setProjectionPort.add(
                emptySet.copy(
                    id = SetId("createdId"),
                    code = SetCode("createdCode"),
                    name = SetName("createdName"),
                    type = SetType("createdType"),
                    icon = SetIcon("createdIcon"),
                    releasedAt = SetReleasedAt(parse("2023-06-12")),
                )
            )
        }

        handler.handle(event)
    }

    @Test
    fun `Handler should listen to SetCreated`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(SetCreated::class)
    }
}
