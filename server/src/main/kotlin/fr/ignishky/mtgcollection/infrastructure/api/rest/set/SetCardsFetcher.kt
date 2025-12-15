package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.CardsResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.PricesResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.api.SetCardsFetcherApi
import mu.KotlinLogging
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SetCardsFetcher(
    private val setApi: SetApiPort,
    private val cardApi: CardApiPort,
) : SetCardsFetcherApi {

    private val logger = KotlinLogging.logger {}

    override fun getCards(setCode: String): ResponseEntity<CardsResponse> {
        logger.info { "Requesting all cards from '$setCode'" }

        val set = setApi.get(SetCode(setCode))
            ?: return ResponseEntity(NOT_FOUND)

        val cards = cardApi.getAll(SetCode(setCode))
        val cardResponses = cards
            .map {
                CardResponse(
                    it.id.value,
                    it.name.value,
                    it.images.value[0].value,
                    it.finishes.isFoil(),
                    it.finishes.isNonFoil(),
                    PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil),
                    it.colors.value.map { it.name },
                    it.nbOwnedNonFoil.value,
                    it.nbOwnedFoil.value,
                )
            }
        val pricesResponse = cards.fold(0L) { totalValue, card -> totalValue + card.maxValuePrice() }

        logger.info { "Returning ${cardResponses.size} cards." }
        return ResponseEntity(CardsResponse(set.name.value, cardResponses, pricesResponse), OK)
    }

}
