package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.COLLECTION_NUMBER

data class CardNumber(
    val value: String
) : CardProperty, Comparable<CardNumber> {

    override fun compareTo(other: CardNumber): Int {
        val a = value.toIntOrNull()
        val b = other.value.toIntOrNull()
        if (a == b) return 0
        if (a == null) return 1
        if (b == null) return -1

        return a.compareTo(b)
    }

    override fun propertyName(): CardProperty.PropertyName {
        return COLLECTION_NUMBER
    }

    override fun propertyValue(): Any {
        return value
    }
}
