package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import fr.ignishky.mtgcollection.configuration.ScryfallProperties
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named
import mu.KotlinLogging.logger
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.lang.Long.parseLong

@Named
class ScryfallCardReferer(
    private val restTemplate: RestTemplate,
    private val properties: ScryfallProperties,
) : CardRefererPort {

    private val logger = logger {}

    override fun getCards(code: SetCode): List<Card> {
        var scryfallCards = emptyList<ScryfallCardData>()

        try {
            var response = restTemplate.getForObject<ScryfallCard>("${properties.baseUrl}/cards/search?order=set&q=e:${code.value}&unique=prints")
            scryfallCards = scryfallCards.plus(response.data)

            while (response.hasMore) {
                response = restTemplate.getForObject(response.nextPage?.replace("%3A", ":") ?: "")
                scryfallCards = scryfallCards.plus(response.data)
            }
        } catch (e: NotFound) {
            logger.warn("Unable to get cards for ${code.value}", e)
        }

        return scryfallCards.map {
            val images = if (it.imageUris != null) listOf(CardImage(it.imageUris.borderCrop.split("?")[0]))
            else it.cardFaces
                ?.map { (imageUris) -> (if (imageUris?.borderCrop != null) imageUris.borderCrop.split("?")[0] else "") }
                ?.map { crop -> CardImage(crop) }
                ?: emptyList()
            Card(
                CardId(it.id),
                CardName(it.name),
                CardSetCode(it.set),
                CardImages(images),
                CardNumber(it.collectionNumber),
                CardPrices(
                    Price(
                        parseLong(it.prices.eur?.replace(".", "") ?: "0"),
                        parseLong(it.prices.eurFoil?.replace(".", "") ?: "0"),
                        parseLong(it.prices.usd?.replace(".", "") ?: "0"),
                        parseLong(it.prices.usdFoil?.replace(".", "") ?: "0"),
                    ),
                ),
                CardFinishes(it.finishes.map(::CardFinish)),
            )
        }
    }

}
