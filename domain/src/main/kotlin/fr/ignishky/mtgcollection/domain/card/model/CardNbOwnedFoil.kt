package fr.ignishky.mtgcollection.domain.card.model

@JvmInline
value class CardNbOwnedFoil(
    val value: Int,
) {
    fun increment(): CardNbOwnedFoil {
        return CardNbOwnedFoil(value.plus(1))
    }

    fun decrease(): CardNbOwnedFoil {
        return CardNbOwnedFoil(value.minus(1))
    }
}
