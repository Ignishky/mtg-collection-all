package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import com.fasterxml.jackson.annotation.JsonProperty

data class ScryfallBulkData(
    val type: String,
    @param:JsonProperty("download_uri")
    val downloadUri: String,
) {
    @Suppress("unused")
    constructor() : this("", "")
}
