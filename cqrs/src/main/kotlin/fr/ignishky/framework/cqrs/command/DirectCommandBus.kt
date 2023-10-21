package fr.ignishky.framework.cqrs.command

import fr.ignishky.framework.cqrs.command.middleware.CommandMiddleware
import fr.ignishky.framework.cqrs.command.middleware.CommandMiddlewareBuilder

class DirectCommandBus(builders: Set<CommandMiddlewareBuilder>) : CommandBus {

    private val chain: CommandMiddleware

    init {
        chain = CommandMiddlewareBuilder.build(builders.stream().toList())
    }

    override fun dispatch(message: Command) {
        chain.handle(message)
    }
}
