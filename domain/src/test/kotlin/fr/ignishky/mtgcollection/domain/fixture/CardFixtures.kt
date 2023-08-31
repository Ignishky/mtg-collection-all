package fr.ignishky.mtgcollection.domain.fixture

import fr.ignishky.mtgcollection.domain.card.model.*

object CardFixtures {

    fun plus2Mace(): Card {
        return Card(
            CardId("e882c9f9-bf30-46b6-bedc-379d2c80e5cb"),
            CardName("+2 Mace"),
            CardSetCode("afr"),
            CardImages(CardImage("https://scryfall.mtgc.test/cards/plus2mace.svg")),
            CardNumber("1"),
            CardPrices(Price(2, 7, 2, 3)),
        )
    }

    fun arboreaPegasus(): Card {
        return Card(
            CardId("fc45c9d4-ecc7-4a9d-9efe-f4b7d697dd97"),
            CardName("Arborea Pegasus"),
            CardSetCode("afr"),
            CardImages(CardImage("https://scryfall.mtgc.test/cards/arboreaPegasus.svg")),
            CardNumber("2"),
            CardPrices(Price(4, 8, 1, 6)),
        )
    }

}
