package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.mtgcollection.configuration.ScryfallProperties
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import jakarta.inject.Named
import org.springframework.http.HttpHeaders.USER_AGENT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.lang.Long.parseLong

@Named
class ScryfallCardReferer(
    private val restClient: RestClient,
    private val properties: ScryfallProperties,
    private val objectMapper: ObjectMapper,
) : CardRefererPort {

    override fun getAllCards(): List<Card> {
        return retrieveCards()
            .map(::toCard)
    }

    private fun retrieveCards(): List<ScryfallCardData> {
        val allCards = restClient.get()
            .uri("${properties.baseUrl}/bulk-data/default_cards")
            .header(USER_AGENT, properties.userAgent)
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ScryfallBulkData>()
            ?: return emptyList()

        return restClient.get()
            .uri(allCards.downloadUri)
            .header(USER_AGENT, properties.userAgent)
            .accept(APPLICATION_JSON)
            .exchange { _, response ->
                objectMapper.readerFor(ScryfallCardData::class.java)
                    .readValues<ScryfallCardData>(response.body)
                    .asSequence()
                    .toList()
            }
    }

    private fun toCard(cardData: ScryfallCardData): Card {
        val images = if (cardData.imageUris != null)
            listOf(CardImage(cardData.imageUris.borderCrop.split("?")[0]))
        else
            cardData.cardFaces
                ?.map { (imageUris) -> (if (imageUris?.borderCrop != null) imageUris.borderCrop.split("?")[0] else "") }
                ?.map { crop -> CardImage(crop) }
                ?: emptyList()
        val colors = when {
            !cardData.colors.isNullOrEmpty() -> cardData.colors
            !cardData.cardFaces.isNullOrEmpty() -> cardData.cardFaces
                .flatMap { it.colors ?: listOf("") }
                .ifEmpty { listOf("") }

            else -> listOf("")
        }

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
            CardColors(colors.distinct().map { CardColor.fromSymbol(it) }),
        )
    }

}
