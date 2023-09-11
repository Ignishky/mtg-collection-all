package fr.ignishky.framework.cqrs.event

interface Payload {

    fun asEvent(aggregateId: String): Event<*, *, *>

}
