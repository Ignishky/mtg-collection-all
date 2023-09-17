package fr.ignishky.mtgcollection.configuration

import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.command.DirectCommandBus
import fr.ignishky.framework.cqrs.command.middleware.CommandDispatcherMiddleware
import fr.ignishky.framework.cqrs.command.middleware.EventDispatcherMiddleware
import fr.ignishky.framework.cqrs.command.middleware.EventPersistenceMiddleware
import fr.ignishky.framework.cqrs.command.middleware.LoggingCommandBusMiddleware
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.EventRepository
import fr.ignishky.framework.domain.CorrelationIdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FrameworkConfiguration {

    @Bean
    fun correlationIdGenerator() = CorrelationIdGenerator()

    @Bean
    fun commandBus(
        eventRepository: EventRepository,
        eventHandlers: List<EventHandler<out Event<*, *, *>>>,
        commandHandlers: List<CommandHandler<*>>,
    ) = DirectCommandBus(
        setOf(
            LoggingCommandBusMiddleware.Builder(),
            EventPersistenceMiddleware.Builder(eventRepository),
            EventDispatcherMiddleware.Builder(eventHandlers),
            CommandDispatcherMiddleware.Builder(commandHandlers)
        )
    )

}
