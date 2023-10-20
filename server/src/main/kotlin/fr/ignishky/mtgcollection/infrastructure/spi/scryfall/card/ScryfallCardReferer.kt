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

    override fun getCards(setCode: SetCode): List<Card> {
        return retrieveCards(setCode)
            .map(::toCard)
    }

    private fun retrieveCards(setCode: SetCode): List<ScryfallCardData> {
        var scryfallCards = emptyList<ScryfallCardData>()

        try {
            var response = restTemplate.getForObject<ScryfallCard>("${properties.baseUrl}/cards/search?order=set&q=e:${setCode.value}&unique=prints")
            scryfallCards = scryfallCards.plus(response.data)

            while (response.hasMore) {
                response = restTemplate.getForObject(response.nextPage?.replace("%3A", ":") ?: "")
                scryfallCards = scryfallCards.plus(response.data)
            }
        } catch (e: NotFound) {
            logger.warn("Unable to get cards for ${setCode.value}", e)
        }
        return scryfallCards
    }

    private fun toCard(cardData: ScryfallCardData): Card {
        val images = if (cardData.imageUris != null)
            listOf(CardImage(cardData.imageUris.borderCrop.split("?")[0]))
        else
            cardData.cardFaces
                ?.map { (imageUris) -> (if (imageUris?.borderCrop != null) imageUris.borderCrop.split("?")[0] else "") }
                ?.map { crop -> CardImage(crop) }
                ?: emptyList()
        return Card(
            CardId(cardData.id),
            CardName(cardData.name),
            CardSetCode(cardData.set),
            CardImages(images),
            CardNumber(cardData.collectionNumber),
            CardPrices(
                Price(
                    parseLong(cardData.prices.eur?.replace(".", "") ?: "0"),
                    parseLong(cardData.prices.eurFoil?.replace(".", "") ?: "0"),
                    parseLong(cardData.prices.usd?.replace(".", "") ?: "0"),
                    parseLong(cardData.prices.usdFoil?.replace(".", "") ?: "0"),
                ),
            ),
            CardFinishes(cardData.finishes.map(::CardFinish)),
        )
    }

}
