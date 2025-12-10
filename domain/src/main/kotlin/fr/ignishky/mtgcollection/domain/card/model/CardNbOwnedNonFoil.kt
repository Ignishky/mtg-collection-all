package fr.ignishky.mtgcollection.domain.card.model

@JvmInline
value class CardNbOwnedNonFoil(
    val value: Int,
) {
    fun increment(): CardNbOwnedNonFoil {
        return CardNbOwnedNonFoil(value.plus(1))
    }

    fun decrease(): CardNbOwnedNonFoil {
        return CardNbOwnedNonFoil(value.minus(1))
    }
}
