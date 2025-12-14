package fr.ignishky.mtgcollection.domain.collection.usecase

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.collection.port.CollectionApiPort
import jakarta.inject.Named

@Named
class GetCollectionCards(
    private val cardProjection: CardProjectionPort,
) : CollectionApiPort {

    override fun getAll(): List<Card> {
        val ownedPriceComparator = compareByDescending<Card> { if (it.nbOwnedFoil.value > 0) it.prices.scryfall.eurFoil else it.prices.scryfall.eur }
        return cardProjection.getCollection().sortedWith(ownedPriceComparator)
    }

}