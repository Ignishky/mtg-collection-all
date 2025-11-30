package fr.ignishky.mtgcollection.domain.card.model

@JvmInline
value class CardNbOwned(
    val value: Int,
) {
    fun increment(): CardNbOwned {
        return CardNbOwned(value.plus(1))
    }
}
