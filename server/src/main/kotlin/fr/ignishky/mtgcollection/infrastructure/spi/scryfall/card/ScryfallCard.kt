package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import com.fasterxml.jackson.annotation.JsonProperty

data class ScryfallCard(
    @JsonProperty("has_more")
    val hasMore: Boolean,
    @JsonProperty("next_page")
    val nextPage: String?,
    val data: List<ScryfallCardData>,
) {

    @Suppress("unused")
    constructor() : this(false, "", listOf())

    data class ScryfallCardData(
        val id: String,
        val name: String,
        val set: String,
        val prices: ScryfallPrices,
        @JsonProperty("image_uris")
        val imageUris: ImageUris?,
        @JsonProperty("card_faces")
        val cardFaces: List<CardFaces>?,
        @JsonProperty("collector_number")
        val collectionNumber: String,
    ) {

        @Suppress("unused")
        constructor() : this(
            "",
            "",
            "",
            ScryfallPrices("0", "0", "0", "0"),
            ImageUris(""),
            emptyList(),
            "",
        )

    }

    data class ScryfallPrices(
        val eur: String?,
        @JsonProperty("eur_foil")
        val eurFoil: String?,
        val usd: String?,
        @JsonProperty("usd_foil")
        val usdFoil: String?,
    ) {

        @Suppress("unused")
        constructor() : this("", "", "", "")

    }

    data class ImageUris(
        @JsonProperty("border_crop")
        val borderCrop: String,
    ) {

        @Suppress("unused")
        constructor() : this("")

    }

    data class CardFaces(
        @JsonProperty("image_uris")
        val imageUris: ImageUris?,
    ) {

        @Suppress("unused")
        constructor() : this(ImageUris(""))

    }

}
