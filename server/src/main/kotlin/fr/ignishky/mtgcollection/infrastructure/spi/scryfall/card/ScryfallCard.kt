package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import com.fasterxml.jackson.annotation.JsonProperty

data class ScryfallCard(
    @param:JsonProperty("has_more")
    val hasMore: Boolean,
    @param:JsonProperty("next_page")
    val nextPage: String?,
    val data: List<ScryfallCardData>,
) {
    @Suppress("unused")
    constructor() : this(false, "", emptyList())
}

data class ScryfallCardData(
    val id: String,
    val name: String,
    val set: String,
    val prices: ScryfallPrices,
    val colors: List<String>?,
    @param:JsonProperty("image_uris")
    val imageUris: ImageUris?,
    @param:JsonProperty("card_faces")
    val cardFaces: List<CardFaces>?,
    val finishes: List<String>,
    @param:JsonProperty("collector_number")
    val collectionNumber: String,
) {
    @Suppress("unused")
    constructor() : this(
        "",
        "",
        "",
        ScryfallPrices("0", "0", "0", "0"),
        emptyList(),
        ImageUris(""),
        emptyList(),
        emptyList(),
        "",
    )
}

data class ScryfallPrices(
    val eur: String?,
    @param:JsonProperty("eur_foil")
    val eurFoil: String?,
    val usd: String?,
    @param:JsonProperty("usd_foil")
    val usdFoil: String?,
) {
    @Suppress("unused")
    constructor() : this("", "", "", "")
}

data class ImageUris(
    @param:JsonProperty("border_crop")
    val borderCrop: String,
) {
    @Suppress("unused")
    constructor() : this("")
}

data class CardFaces(
    @param:JsonProperty("image_uris")
    val imageUris: ImageUris?,
    val colors: List<String>?
) {
    @Suppress("unused")
    constructor() : this(ImageUris(""), emptyList())
}
