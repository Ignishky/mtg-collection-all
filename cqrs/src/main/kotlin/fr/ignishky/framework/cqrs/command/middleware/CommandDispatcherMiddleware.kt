package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler

class CommandDispatcherMiddleware(
    next: CommandMiddleware,
    handlers: List<CommandHandler<*>>,
) : CommandMiddleware(next) {

    private val handlersByCommand = handlers.associateBy { it.listenTo() }

    override fun handle(command: Command) = handlersByCommand.getValue(command::class).handle(command)

    class Builder(
        private val handlers: List<CommandHandler<*>>,
    ) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware) = CommandDispatcherMiddleware(next, handlers)

    }

}
