package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.axgardBraggart
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.card.event.CardDisowned
import fr.ignishky.mtgcollection.domain.card.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.infrastructure.AbstractIT
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.json.JsonCompareMode.STRICT
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CollectionApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    @Test
    fun should_add_non_foil_card_to_the_collection() {
        givenSets(afr)
        givenCards(plus2Mace)

        val results = mockMvc.perform(
            put("/collection/${plus2Mace.id.value}")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": false
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(false)))
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(false)))
    }

    @Test
    fun should_add_foil_card_to_the_collection() {
        givenSets(afr)
        givenCards(plus2Mace)

        val results = mockMvc.perform(
            put("/collection/${plus2Mace.id.value}")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": true
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(true)))
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
    }

    @Test
    fun should_remove_card_from_the_collection() {
        givenSets(afr)
        givenCards(plus2Mace.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(true)))

        val results = mockMvc.perform(
            delete("/collection/${plus2Mace.id.value}")
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace.copy(isOwned = CardIsOwned(false), isOwnedFoil = CardIsOwnedFoil(false)))
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardDisowned(plus2Mace.id))
    }

    @Test
    fun should_retrieve_owned_cards() {
        givenSets(afr, khm)
        givenCards(
            arboreaPegasus.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(true)),
            plus2Mace.copy(isOwned = CardIsOwned(true)),
            axgardBraggart.copy(isOwned = CardIsOwned(false))
        )

        val results = mockMvc.perform(get("/collection"))

        results.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("collection/cardsResponse.json"), STRICT)
        )
    }

}
