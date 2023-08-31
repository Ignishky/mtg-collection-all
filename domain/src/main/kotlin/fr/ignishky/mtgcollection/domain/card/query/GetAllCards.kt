package fr.ignishky.mtgcollection.domain.card.query

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named

@Named
class GetAllCards(private val cardStore: CardStorePort) : CardApiPort {

    override fun getAll(setCode: SetCode): List<Card> {
        return cardStore.get(setCode).sortedBy { card: Card -> card.collectionNumber }
    }

}
