package fr.ignishky.mtgcollection.domain.fixture

import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import java.time.LocalDate.parse

object SetFixtures {

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

}
