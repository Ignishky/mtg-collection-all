package fr.ignishky.framework.cqrs.event

import kotlin.reflect.KClass

interface EventHandler<I : Event<*, *, *>> {

    fun handle(event: Event<*, *, *>)

    fun listenTo(): KClass<I>

}
