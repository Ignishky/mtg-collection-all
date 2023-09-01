package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.CardsResponse.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.set.SetsResponse.SetResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class SetController(
    private val setApiPort: SetApiPort,
    private val cardApiPort: CardApiPort,
) : SetApi {

    override fun getAll(correlationId: CorrelationId): SetsResponse {
        val sets = setApiPort.getAll()
        return SetsResponse(
            sets.map { SetResponse(it.code.value, it.name.value, it.type.value, it.icon.value) }
        )
    }

    override fun getCards(correlationId: CorrelationId, setCode: String): CardsResponse {
        val cards = cardApiPort.getAll(SetCode(setCode))
        return CardsResponse(
            cards.map {
                CardResponse(
                    it.name.value,
                    it.images.value[0].value,
                    CardsResponse.PricesResponse(it.prices.scryfall.eur, it.prices.scryfall.eurFoil)
                )
            }
        )
    }

}
