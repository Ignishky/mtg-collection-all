package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventRepository
import fr.ignishky.framework.domain.CorrelationId
import mu.KotlinLogging.logger

class EventPersistenceMiddleware(
    next: CommandMiddleware,
    private val eventRepository: EventRepository,
) : CommandMiddleware(next) {

    private val logger = logger {}

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        val events = next(command, correlationId)

        logger.info { "Saving ${events.size} events" }

        eventRepository.saveAll(events)

        return events
    }

    class Builder(private val eventRepository: EventRepository) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware) = EventPersistenceMiddleware(next, eventRepository)
    }

}
