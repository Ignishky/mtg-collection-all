package fr.ignishky.framework.domain

interface Aggregate<I : AggregateId> {

    fun id(): I

}
