package fr.ignishky.mtgcollection.domain.set.command

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.domain.set.model.SetType
import fr.ignishky.mtgcollection.domain.set.port.SetEventStorePort
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import fr.ignishky.mtgcollection.domain.set.usecase.RefreshSet
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RefreshSetTest {

    private val correlationId = CorrelationId("test-correlation-id")

    private val setReferer = mockk<SetRefererPort>()
    private val setEventStorePort = mockk<SetEventStorePort>()
    private val handler = RefreshSet.RefreshSetHandler(setReferer, setEventStorePort)

    @Test
    fun `Should return no event when set is unmodified`() {
        every { setEventStorePort.getAll() } returns listOf(afr())
        every { setReferer.getAllSets() } returns listOf(afr())

        val events = handler.handle(RefreshSet(), correlationId)

        assertThat(events).isEmpty()
    }

    @Test
    fun `Should return SetCreated event when a referer set is not stored`() {
        every { setEventStorePort.getAll() } returns emptyList()
        every { setReferer.getAllSets() } returns listOf(afr())

        val events = handler.handle(RefreshSet(), correlationId)

        assertThat(events)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "instant")
            .containsOnly(SetCreated(correlationId, afr().id, afr().code, afr().name, afr().type, afr().icon, afr().releasedAt))
    }

    @Test
    fun `Should return SetUpdated event when a referer set is stored differently`() {
        every { setEventStorePort.getAll() } returns listOf(afr().copy(name = SetName("Old name"), type = SetType("Old type")))
        every { setReferer.getAllSets() } returns listOf(afr())

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
