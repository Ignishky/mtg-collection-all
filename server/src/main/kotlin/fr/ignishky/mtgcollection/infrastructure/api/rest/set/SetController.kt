package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.CardsResponse.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.SetsResponse.SetResponse
import mu.KotlinLogging
import org.springframework.web.bind.annotation.RestController

@RestController
class SetController(
    private val setApiPort: SetApiPort,
    private val cardApiPort: CardApiPort,
) : SetApi {

    private val logger = KotlinLogging.logger {}

    override fun getAll(correlationId: CorrelationId): SetsResponse {
        logger.info { "Requesting all sets ..." }

        val sets = setApiPort.getAll()
            .map { SetResponse(it.code.value, it.name.value, it.type.value, it.icon.value) }

        logger.info { "Returning ${sets.size} sets." }
        return SetsResponse(sets)
    }

    override fun getCards(correlationId: CorrelationId, setCode: String): CardsResponse {
        logger.info { "Requesting all cards from '${setCode}'" }

        val cards = cardApiPort.getAll(SetCode(setCode))
            .map {
                CardResponse(
                    it.name.value,
                    it.images.value[0].value,
                    CardsResponse.PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil)
                )
            }

        logger.info { "Returning ${cards.size} cards." }
        return CardsResponse(cards)
    }

}
