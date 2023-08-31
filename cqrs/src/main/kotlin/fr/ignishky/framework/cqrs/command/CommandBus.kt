package fr.ignishky.framework.cqrs.command

import fr.ignishky.framework.domain.CorrelationId

interface CommandBus {

    fun dispatch(message: Command, correlationId: CorrelationId)

}
