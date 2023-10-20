package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.card.exception.NoCardFoundException
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named

@Named
class GetAllCards(
    private val cardProjectionPort: CardProjectionPort,
) : CardApiPort {

    override fun getAll(setCode: SetCode): List<Card> {
        val cards = cardProjectionPort.getAll(setCode)
        if (cards.isEmpty()) {
            throw NoCardFoundException(setCode)
        }
        return cards.sortedBy { card: Card -> card.collectionNumber }
    }

}
