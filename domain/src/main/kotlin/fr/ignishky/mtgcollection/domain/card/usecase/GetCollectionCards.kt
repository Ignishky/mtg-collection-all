package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.card.port.CollectionApiPort
import jakarta.inject.Named

@Named
class GetCollectionCards(
    val cardProjectionPort: CardProjectionPort,
) : CollectionApiPort{

    override fun getAll(): List<Card> {
        return cardProjectionPort.getCollection()
    }

}
