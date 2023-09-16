package fr.ignishky.framework.cqrs.event

interface EventRepository {
    fun saveAll(events: List<Event<*, *, *>>)
}
