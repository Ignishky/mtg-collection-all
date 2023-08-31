package fr.ignishky.framework.cqrs.event

import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.framework.domain.AggregateId
import java.time.Instant
import kotlin.reflect.KClass

abstract class Event<I : AggregateId, A : Aggregate<I>, P : Payload>
protected constructor(
    private val id: Long,
    val aggregateId: I,
    val aggregateClass: KClass<A>,
    val payload: P,
    val instant: Instant
) {

    abstract fun apply(aggregate: A): A

    override fun toString(): String {
        return "Event($id, $aggregateId, $aggregateClass, $payload, $instant)"
    }

}
