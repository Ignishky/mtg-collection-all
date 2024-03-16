package fr.ignishky.framework.cqrs.command

fun interface CommandBus {

    fun dispatch(message: Command)

}
