package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.set

import com.fasterxml.jackson.annotation.JsonProperty

data class ScryfallSet(
    val data: List<ScryfallSetData>
) {

    @Suppress("unused")
    constructor() : this(listOf())

    data class ScryfallSetData(
        val id: String,
        val code: String,
        val name: String,
        @JsonProperty("released_at")
        val releasedAt: String,
        @JsonProperty("set_type")
        val setType: String,
        @JsonProperty("icon_svg_uri")
        val iconSvgUri: String,
    ) {

        @Suppress("unused")
        constructor() : this("", "", "", "", "", "")

    }

}
