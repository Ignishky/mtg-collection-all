package fr.ignishky.mtgcollection.domain.collection.usecase

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.collection.model.CardCollection
import fr.ignishky.mtgcollection.domain.collection.port.CollectionApiPort
import jakarta.inject.Named

@Named
class GetCollectionCards(
    private val cardProjection: CardProjectionPort,
) : CollectionApiPort {

    override fun getCollection(): CardCollection {
        val ownedPriceComparator =
            compareByDescending<Card> { if (it.nbOwnedFoil.value > 0) it.prices.scryfall.eurFoil else it.prices.scryfall.eur }
        val sortedCards = cardProjection.getCollection().sortedWith(ownedPriceComparator)
        val (nbOwned, valueOwned) = sortedCards.fold(Pair(0, 0L)) { acc, card ->
            Pair(
                acc.first + card.nbOwned(),
                acc.second + card.valueOwned()
            )
        }
        return CardCollection(sortedCards, nbOwned, valueOwned)
    }

}