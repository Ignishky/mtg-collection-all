package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId

class CircuitBreakerMiddleware internal constructor() : CommandMiddleware(null) {

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        throw IllegalStateException("No final command middleware provided in the chain")
    }

}
