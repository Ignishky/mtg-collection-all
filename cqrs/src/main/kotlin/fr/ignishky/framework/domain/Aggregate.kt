package fr.ignishky.framework.domain

fun interface Aggregate<I : AggregateId> {

    fun id(): I

}
