package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.set

import fr.ignishky.mtgcollection.configuration.ScryfallProperties
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import jakarta.inject.Named
import org.springframework.http.HttpHeaders.USER_AGENT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.client.RestClient
import java.time.LocalDate.parse

@Named
class ScryfallSetReferer(
    private val restClient: RestClient,
    private val properties: ScryfallProperties,
) : SetRefererPort {

    override fun getAllSets() = (restClient.get()
        .uri("${properties.baseUrl}/sets")
        .header(USER_AGENT, properties.userAgent)
        .accept(APPLICATION_JSON)
        .retrieve()
        .body(ScryfallSet::class.java)
        ?: ScryfallSet(emptyList()))
        .data
        .map {
            Set(
                SetId(it.id),
                SetCode(it.code),
                SetName(it.name),
                SetType(it.setType),
                SetIcon(it.iconSvgUri.split("?")[0]),
                SetReleasedAt(parse(it.releasedAt)),
                it.parentCode?.let { parentCode -> SetParentCode(parentCode) },
                SetNbCards(it.cardCount),
                SetNbOwnedCards(0),
            )
        }

}
