package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.CorrelationId

class EventDispatcherMiddleware(
    next: CommandMiddleware,
    handlers: List<EventHandler<out Event<*, *, *>>>
) : CommandMiddleware(next) {

    private val handlersByEvent = handlers.associateBy { it.listenTo() }

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        val events = next(command, correlationId)

        events.parallelStream().forEach {
            handlersByEvent.getValue(it::class).handle(it)
        }

        return events
    }

    class Builder(
        private val handlers: List<EventHandler<out Event<*, *, out Payload>>>
    ) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware): EventDispatcherMiddleware {
            return EventDispatcherMiddleware(next, handlers)
        }

    }

}
