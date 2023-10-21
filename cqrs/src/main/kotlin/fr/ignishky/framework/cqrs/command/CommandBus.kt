package fr.ignishky.framework.cqrs.command

interface CommandBus {

    fun dispatch(message: Command)

}
