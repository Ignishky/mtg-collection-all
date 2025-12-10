package fr.ignishky.mtgcollection.domain.card.usecase

import fr.ignishky.mtgcollection.domain.CardFixtures.axgardBraggart
import fr.ignishky.mtgcollection.domain.CardFixtures.valorSinger
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedNonFoil
import fr.ignishky.mtgcollection.domain.card.model.CardPrices
import fr.ignishky.mtgcollection.domain.card.model.Price
import fr.ignishky.mtgcollection.domain.card.port.CardProjectionPort
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetCollectionCardsTest {

    private val cardProjection = mockk<CardProjectionPort>()
    private val query = GetCollectionCards(cardProjection)

    @Test
    fun return_owned_cards_sorted_by_collection_price_decreasing() {
        val axgardBraggart = axgardBraggart.copy(
            prices = CardPrices(Price(1, 2, 1, 3)),
            nbOwnedFoil = CardNbOwnedFoil(1),
        )
        val valorSinger = valorSinger.copy(
            prices = CardPrices(Price(1, 14, 1, 3)),
            nbOwnedNonFoil = CardNbOwnedNonFoil(1),
        )
        every { cardProjection.getCollection() } returns listOf(valorSinger, axgardBraggart)

        val cards = query.getAll()

        assertThat(cards).containsExactly(axgardBraggart, valorSinger)
    }
}
