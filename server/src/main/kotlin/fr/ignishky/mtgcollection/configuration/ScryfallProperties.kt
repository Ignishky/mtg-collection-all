package fr.ignishky.mtgcollection.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("scryfall")
data class ScryfallProperties(
    internal val baseUrl: String,
)
