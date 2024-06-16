package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import mu.KotlinLogging
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SetController(
    private val setApi: SetApiPort,
    private val cardApi: CardApiPort,
) : SetApi {

    private val logger = KotlinLogging.logger {}

    override fun getAll(): ResponseEntity<SetsResponse> {
        logger.info { "Requesting all sets ..." }

        val sets = setApi.getAll()
            .map { SetResponse(it.code.value, it.name.value, it.type.value, it.icon.value) }

        logger.info { "Returning ${sets.size} sets." }
        return ResponseEntity(SetsResponse(sets), OK)
    }

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
                    it.isOwned.value,
                    it.isOwnedFoil.value,
                )
            }
        val pricesResponse = cards.fold(PricesResponse(0, 0)) { (eur, eurFoil), card ->
            PricesResponse(eur + card.minEurPrice(), eurFoil + card.maxEurPrice())
        }

        logger.info { "Returning ${cardResponses.size} cards." }
        return ResponseEntity(CardsResponse(set.name.value, cardResponses, pricesResponse), OK)
    }

}
