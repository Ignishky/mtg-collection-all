package fr.ignishky.mtgcollection.domain

import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import java.time.LocalDate.parse

object SetFixtures {

    private val afrIcon = SetIcon("https://scryfall.mtgc.test/sets/afr.svg")
    private val afrReleasedAt = SetReleasedAt(parse("2021-07-23"))

    val afr = Set(
        SetId("e1ef87ba-ba92-4573-817f-543b996d2851"),
        SetCode("afr"),
        SetName("Adventures in the Forgotten Realms"),
        SetType("expansion"),
        afrIcon,
        afrReleasedAt,
    )

    val pafr = Set(
        SetId("983f027f-76f9-462d-8eb5-458dcbf029b4"),
        SetCode("pafr"),
        SetName("Adventures in the Forgotten Realms Promos"),
        SetType("promo"),
        afrIcon,
        afrReleasedAt,
    )

    val aafr = Set(
        SetId("8696ef63-3530-4453-a0f8-7fd3bd09306a"),
        SetCode("aafr"),
        SetName("Adventures in the Forgotten Realms Art Series"),
        SetType("memorabilia"),
        afrIcon,
        afrReleasedAt,
    )

    val khm = Set(
        SetId("43057fad-b1c1-437f-bc48-0045bce6d8c9"),
        SetCode("khm"),
        SetName("Kaldheim"),
        SetType("expansion"),
        SetIcon("https://scryfall.mtgc.test/sets/khm.svg"),
        SetReleasedAt(parse("2021-02-05")),
    )

    val tkhm = Set(
        SetId("c3ee48f1-6f93-42d4-b05c-65a04d02a488"),
        SetCode("tkhm"),
        SetName("Kaldheim Tokens"),
        SetType("token"),
        SetIcon("https://scryfall.mtgc.test/sets/khm.svg"),
        SetReleasedAt(parse("2021-02-05")),
    )

}
