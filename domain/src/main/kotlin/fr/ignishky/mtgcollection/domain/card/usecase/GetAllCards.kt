package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named

@Named
class GetAllCards(
    private val cardProjection: CardProjectionPort,
) : CardApiPort {

    override fun getAll(setCode: SetCode) = cardProjection.getAll(setCode)
        .sortedBy { card -> card.collectionNumber }

}
