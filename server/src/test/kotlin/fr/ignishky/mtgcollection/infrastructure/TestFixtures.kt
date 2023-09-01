package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import java.time.LocalDate.parse

object TestFixtures {

    fun afr(): Set {
        return Set(
            SetId("e1ef87ba-ba92-4573-817f-543b996d2851"),
            SetCode("afr"),
            SetName("Adventures in the Forgotten Realms"),
            SetType("expansion"),
            SetIcon("https://scryfall.mtgc.test/sets/afr.svg"),
            SetReleasedAt(parse("2021-07-23")),
        )
    }

    fun pafr(): Set {
        return Set(
            SetId("983f027f-76f9-462d-8eb5-458dcbf029b4"),
            SetCode("pafr"),
            SetName("Adventures in the Forgotten Realms Promos"),
            SetType("promo"),
            SetIcon("https://scryfall.mtgc.test/sets/afr.svg"),
            SetReleasedAt(parse("2021-07-23")),
        )
    }

    fun aafr(): Set {
        return Set(
            SetId("8696ef63-3530-4453-a0f8-7fd3bd09306a"),
            SetCode("aafr"),
            SetName("Adventures in the Forgotten Realms Art Series"),
            SetType("memorabilia"),
            SetIcon("https://scryfall.mtgc.test/sets/afr.svg"),
            SetReleasedAt(parse("2021-07-23")),
        )
    }

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

    fun flumph(): Card {
        return Card(
            CardId("cdc86e78-8911-4a0d-ba3a-7802f8d991ef"),
            CardName("Flumph"),
            CardSetCode("afr"),
            CardImages(CardImage("https://scryfall.mtgc.test/cards/flumph.svg")),
            CardNumber("15"),
            CardPrices(Price(5, 9, 2, 7)),
        )
    }

    fun specialPegasus(): Card {
        return Card(
            CardId("4e076b7f-14f3-4ce2-83f9-a18ccb2b755f"),
            CardName("Special Pegasus"),
            CardSetCode("afr"),
            CardImages(CardImage("https://scryfall.mtgc.test/cards/specialPegasus.svg")),
            CardNumber("S2"),
            CardPrices(Price(6, 10, 3, 8)),
        )
    }

    fun khm(): Set {
        return Set(
            SetId("43057fad-b1c1-437f-bc48-0045bce6d8c9"),
            SetCode("khm"),
            SetName("Kaldheim"),
            SetType("expansion"),
            SetIcon("https://scryfall.mtgc.test/sets/khm.svg"),
            SetReleasedAt(parse("2021-02-05")),
        )
    }

    fun valorSinger(): Card {
        return Card(
            CardId("89bc162c-bdf1-43f7-882f-d8cee4f3f415"),
            CardName("Valor Singer"),
            CardSetCode("afr"),
            CardImages(CardImage("https://scryfall.mtgc.test/cards/valorSinger.svg")),
            CardNumber("165"),
            CardPrices(Price(1, 14, 1, 3)),
        )
    }

    fun axgardBraggart(): Card {
        return Card(
            CardId("4de5ff64-6fe7-4fc5-be27-cdbaa14545ab"),
            CardName("Axgard Braggart"),
            CardSetCode("khm"),
            CardImages(CardImage("https://scryfall.mtgc.test/cards/axgardBraggart.svg")),
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

    fun tkhm(): Set {
        return Set(
            SetId("c3ee48f1-6f93-42d4-b05c-65a04d02a488"),
            SetCode("tkhm"),
            SetName("Kaldheim Tokens"),
            SetType("token"),
            SetIcon("https://scryfall.mtgc.test/sets/khm.svg"),
            SetReleasedAt(parse("2021-02-05")),
        )
    }

}
