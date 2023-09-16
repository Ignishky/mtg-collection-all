package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.set.event.SetCreated.SetCreatedPayload
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import mu.KotlinLogging.logger
import java.time.Instant.now
import java.time.LocalDate
import kotlin.reflect.KClass

class SetCreated(
    correlationId: CorrelationId,
    aggregateId: SetId,
    code: SetCode,
    name: SetName,
    type: SetType,
    icon: SetIcon,
    releasedAt: SetReleasedAt,
) :
    Event<SetId, Set, SetCreatedPayload>(
        0,
        aggregateId,
        Set::class,
        SetCreatedPayload(
            code.value,
            name.value,
            type.value,
            icon.value,
            releasedAt.value.toString(),
        ),
        now(),
        correlationId,
    ) {

    override fun apply(aggregate: Aggregate<*>): Set {
        return Set(
            aggregateId,
            SetCode(payload.code),
            SetName(payload.name),
            SetType(payload.type),
            SetIcon(payload.icon),
            SetReleasedAt(LocalDate.parse(payload.releasedAt)),
        )
    }

    data class SetCreatedPayload(
        val code: String,
        val name: String,
        val type: String,
        val icon: String,
        val releasedAt: String,
    ) : Payload

    @Named
    class SetCreatedHandler(private val setStore: SetStorePort) : EventHandler<SetCreated> {

        private val logger = logger {}

        override fun handle(event: Event<*, *, *>) {
            val setCreated = event as SetCreated
            logger.info { "Creating set '${setCreated.payload.name}'..." }
            setStore.store(setCreated.apply(Set()))
        }

        override fun listenTo(): KClass<SetCreated> {
            return SetCreated::class
        }

    }

}
