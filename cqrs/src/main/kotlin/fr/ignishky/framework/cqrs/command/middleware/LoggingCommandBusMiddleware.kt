package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import mu.KotlinLogging

class LoggingCommandBusMiddleware(next: CommandMiddleware) : CommandMiddleware(next) {

    private val logger = KotlinLogging.logger {}

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        logger.info("Executing {}", command::class.simpleName)
        return try {
            val next = next(command, correlationId)
            logger.info("Success on {}. Events : {}", command::class.simpleName, next)
            next
        } catch (throwable: Throwable) {
            logger.error("Error on {}", command::class.simpleName, throwable)
            throw throwable
        }
    }

    class Builder : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware) = LoggingCommandBusMiddleware(next)

    }

}
