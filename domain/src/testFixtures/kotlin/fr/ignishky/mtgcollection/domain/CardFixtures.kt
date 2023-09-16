package fr.ignishky.mtgcollection.domain

import fr.ignishky.mtgcollection.domain.card.model.*

object CardFixtures {

    fun plus2Mace(): Card {
        return Card(
            CardId("e882c9f9-bf30-46b6-bedc-379d2c80e5cb"),
            CardName("+2 Mace"),
            CardSetCode("afr"),
            CardImages(listOf(CardImage("https://scryfall.mtgc.test/cards/plus2mace.svg"))),
            CardNumber("1"),
            CardPrices(Price(2, 7, 2, 3)),
        )
    }

    fun arboreaPegasus(): Card {
        return Card(
            CardId("fc45c9d4-ecc7-4a9d-9efe-f4b7d697dd97"),
            CardName("Arborea Pegasus"),
            CardSetCode("afr"),
            CardImages(listOf(CardImage("https://scryfall.mtgc.test/cards/arboreaPegasus.svg"))),
            CardNumber("2"),
            CardPrices(Price(4, 8, 1, 6)),
        )
    }

    fun valorSinger(): Card {
        return Card(
            CardId("89bc162c-bdf1-43f7-882f-d8cee4f3f415"),
            CardName("Valor Singer"),
            CardSetCode("afr"),
            CardImages(listOf(CardImage("https://scryfall.mtgc.test/cards/valorSinger.svg"))),
            CardNumber("165"),
            CardPrices(Price(1, 14, 1, 3)),
        )
    }

    fun axgardBraggart(): Card {
        return Card(
            CardId("4de5ff64-6fe7-4fc5-be27-cdbaa14545ab"),
            CardName("Axgard Braggart"),
            CardSetCode("khm"),
            CardImages(listOf(CardImage("https://scryfall.mtgc.test/cards/axgardBraggart.svg"))),
            CardNumber("1"),
            CardPrices(Price(1, 2, 0, 0)),
        )
    }

    fun halvar(): Card {
        return Card(
            CardId("97502411-5c93-434c-b77b-ceb2c32feae7"),
            CardName("Halvar, God of Battle // Sword of the Realms"),
            CardSetCode("khm"),
            CardImages(
                listOf(
                    CardImage("https://scryfall.mtgc.test/cards/halvar.svg"),
                    CardImage("https://scryfall.mtgc.test/cards/swordOfTheRealms.svg")
                )
            ),
            CardNumber("15"),
            CardPrices(Price(796, 699, 692, 647)),
        )
    }

}
