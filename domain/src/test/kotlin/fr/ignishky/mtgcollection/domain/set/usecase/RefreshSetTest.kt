package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.domain.set.model.SetType
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshSetTest {

    private val correlationId = CorrelationId("test-correlation-id")

    private val setReferer = mockk<SetRefererPort>()
    private val setProjectionPort = mockk<SetProjectionPort>()
    private val handler = RefreshSetHandler(setReferer, setProjectionPort)

    @Test
    fun `Should return no event when set is unmodified`() {
        every { setProjectionPort.getAll() } returns listOf(afr())
        every { setReferer.getAllSets() } returns listOf(afr())

        val events = handler.handle(RefreshSet(), correlationId)

        assertThat(events).isEmpty()
    }

    @Test
    fun `Should return SetCreated event when a referer set is not stored`() {
        every { setProjectionPort.getAll() } returns emptyList()
        every { setReferer.getAllSets() } returns listOf(afr())
        justRun { setProjectionPort.add(afr()) }

        val events = handler.handle(RefreshSet(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
            .containsOnly(SetCreated(correlationId, afr().id, afr().code, afr().name, afr().type, afr().icon, afr().releasedAt))
    }

    @Test
    fun `Should return SetUpdated event when a referer set is stored differently`() {
        every { setProjectionPort.getAll() } returns listOf(afr().copy(name = SetName("Old name"), type = SetType("Old type")))
        every { setReferer.getAllSets() } returns listOf(afr())
        justRun { setProjectionPort.update(afr().id, listOf(afr().name, afr().type)) }

        val events = handler.handle(RefreshSet(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
            .containsOnly(SetUpdated(correlationId, afr().id, afr().name, afr().type))
    }

    @Test
    fun `Handler should listen to RefreshSet`() {
        val listenTo = handler.listenTo()

        assertThat(listenTo).isEqualTo(RefreshSet::class)
    }
}
