package fr.ignishky.framework.cqrs.event

import fr.ignishky.framework.domain.AggregateId

interface EventRepository {
    fun saveAll(events: List<Event<*, *, *>>)

    fun getAll(id: AggregateId): List<Event<*, *, *>>
}
