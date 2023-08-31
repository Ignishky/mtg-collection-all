package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId

abstract class CommandMiddleware internal constructor(private val next: CommandMiddleware?) {

    abstract fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>>

    protected fun next(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        return next!!.handle(command, correlationId)
    }

}
