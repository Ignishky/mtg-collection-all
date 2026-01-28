package fr.ignishky.mtgcollection.infrastructure.api.rest.common

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.PricesResponse

object CardResponseMapper {

    fun Card.toCardResponse() =
        CardResponse(
            id.value,
            setCode.value,
            collectionNumber.value,
            name.value,
            images.value[0].value,
            finishes.isFoil(),
            finishes.isNonFoil(),
            PricesResponse(prices.scryfall.eur, prices.scryfall.eurFoil),
            colors.value.map { it.name },
            nbOwnedNonFoil.value,
            nbOwnedFoil.value,
        )
}