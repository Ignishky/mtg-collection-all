package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.framework.domain.AggregateId

data class SetId(
    val value: String,
) : AggregateId {

    override fun value() = value

}
