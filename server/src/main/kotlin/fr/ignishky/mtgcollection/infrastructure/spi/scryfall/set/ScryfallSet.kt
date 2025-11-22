package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.set

import com.fasterxml.jackson.annotation.JsonProperty

data class ScryfallSet(
    val data: List<ScryfallSetData>,
) {

    @Suppress("unused")
    constructor() : this(emptyList())

}

data class ScryfallSetData(
    val id: String,
    val code: String,
    val name: String,
    @param:JsonProperty("released_at")
    val releasedAt: String,
    @param:JsonProperty("set_type")
    val setType: String,
    @param:JsonProperty("icon_svg_uri")
    val iconSvgUri: String,
    @param:JsonProperty("parent_set_code")
    val parentCode: String?,
) {

    @Suppress("unused")
    constructor() : this("", "", "", "", "", "", "")

}
