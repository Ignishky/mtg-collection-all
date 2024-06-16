package fr.ignishky.mtgcollection.domain.card.model

data class Price(
    val eur: Long,
    val eurFoil: Long,
    val usd: Long,
    val usdFoil: Long,
) {
    fun update(updatePrice: Price): Price {
        return Price(
            eur = if (updatePrice.eur != 0L) updatePrice.eur else eur,
            eurFoil = if (updatePrice.eurFoil != 0L) updatePrice.eurFoil else eurFoil,
            usd = if (updatePrice.usd != 0L) updatePrice.usd else usd,
            usdFoil = if (updatePrice.usdFoil != 0L) updatePrice.usdFoil else usdFoil,
        )
    }
}
