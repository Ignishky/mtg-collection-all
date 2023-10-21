package fr.ignishky.framework.cqrs.command

import fr.ignishky.framework.cqrs.event.Event
import kotlin.reflect.KClass

interface CommandHandler<T : Command> {

    fun handle(command: Command): List<Event<*, *, *>>

    fun listenTo(): KClass<out T>
}
