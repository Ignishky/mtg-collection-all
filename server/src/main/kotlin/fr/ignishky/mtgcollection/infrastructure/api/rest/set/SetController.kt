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
    private val setApiPort: SetApiPort,
    private val cardApiPort: CardApiPort,
) : SetApi {

    private val logger = KotlinLogging.logger {}

    override fun getAll(): SetsResponse {
        logger.info { "Requesting all sets ..." }

        val sets = setApiPort.getAll()
            .map { SetResponse(it.code.value, it.name.value, it.type.value, it.icon.value) }

        logger.info { "Returning ${sets.size} sets." }
        return SetsResponse(sets)
    }

    override fun getCards(setCode: String): ResponseEntity<CardsResponse> {
        logger.info { "Requesting all cards from '${setCode}'" }

        val set = setApiPort.get(SetCode(setCode))
            ?: return ResponseEntity(NOT_FOUND)

        val cards = cardApiPort.getAll(SetCode(setCode))
            .map {
                CardResponse(
                    it.id.value,
                    it.name.value,
                    it.images.value[0].value,
                    it.finishes.isFoil(),
                    it.finishes.isNonFoil(),
                    PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil)
                )
            }
        val pricesResponse = cards.fold(PricesResponse(0, 0)) { (eur, eurFoil), card ->
            PricesResponse(eur + card.prices.eur, eurFoil + card.prices.eurFoil)
        }

        logger.info { "Returning ${cards.size} cards." }
        return ResponseEntity(CardsResponse(set.name.value, cards, pricesResponse), OK)
    }

}
