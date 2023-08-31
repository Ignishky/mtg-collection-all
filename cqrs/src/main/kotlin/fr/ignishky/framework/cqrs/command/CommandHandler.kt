package fr.ignishky.framework.cqrs.command

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import kotlin.reflect.KClass

interface CommandHandler<T : Command> {

    fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>>

    fun listenTo(): KClass<out T>
}
