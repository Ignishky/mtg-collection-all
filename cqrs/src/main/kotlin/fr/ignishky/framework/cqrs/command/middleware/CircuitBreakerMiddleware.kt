package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event

class CircuitBreakerMiddleware internal constructor() : CommandMiddleware(null) {

    override fun handle(command: Command): List<Event<*, *, *>> {
        throw IllegalStateException("No final command middleware provided in the chain")
    }

}
