package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.set

import fr.ignishky.mtgcollection.configuration.ScryfallProperties
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import jakarta.inject.Named
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.time.LocalDate.parse

@Named
class ScryfallSetReferer(
    private val restTemplate: RestTemplate,
    private val properties: ScryfallProperties,
) : SetRefererPort {

    override fun getAllSets() = restTemplate.getForObject<ScryfallSet>("${properties.baseUrl}/sets")
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
            )
        }

}
