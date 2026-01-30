package fr.ignishky.mtgcollection.domain.set.usecase

import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import fr.ignishky.mtgcollection.domain.set.port.SetSummary
import jakarta.inject.Named

@Named
class GetAllSets(
    private val setProjection: SetProjectionPort,
    private val cardProjectionPort: CardProjectionPort,
) : SetApiPort {

    override fun getAll() = setProjection.getAll()
        .filter { hasOwnIcon(it) }
        .sortedByDescending { it.releasedAt }
        .map {
            val cards = cardProjectionPort.getAll(it.code)
            val nbOwnedCards = cards.filter { it.nbOwned() > 0 }.size
            it.toSetSummary(cards.size, nbOwnedCards)
        }

    private fun hasOwnIcon(it: Set) = it.icon.value.contains(it.code.value)

    override fun get(setCode: SetCode): SetSummary? {
        return setProjection.get(setCode)?.toSetSummary()
    }
}

fun Set.toSetSummary(nbCards: Int = 0, nbOwnedCards: Int = 0) = SetSummary(code, name, icon, nbCards, nbOwnedCards)