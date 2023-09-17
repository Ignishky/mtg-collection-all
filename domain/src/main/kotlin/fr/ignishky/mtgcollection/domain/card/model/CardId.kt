package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.framework.domain.AggregateId

data class CardId(
    val value: String,
) : AggregateId {

    override fun value() = value

}
