package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.axgardBraggart
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.CardFixtures.valorSinger
import fr.ignishky.mtgcollection.domain.SetFixtures.aafr
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.SetFixtures.pafr
import fr.ignishky.mtgcollection.domain.SetFixtures.tkhm
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.infrastructure.AbstractIT
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.junit.jupiter.api.Test
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.json.JsonCompareMode.STRICT
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@MockServerTest("scryfall.base-url=http://localhost:\${mockServerPort}")
internal class SetApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    @Test
    fun should_return_sets() {
        // GIVEN
        givenSets(khm, tkhm, afr, aafr, pafr)

        // WHEN
        val resultActions = mockMvc.perform(get("/sets"))

        // THEN
        resultActions.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("sets/setsResponse.json"), STRICT)
        )
    }

    @Test
    fun should_return_cards_from_given_set() {
        // GIVEN
        givenSets(afr)
        givenCards(
            arboreaPegasus.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(true)),
            plus2Mace.copy(isOwned = CardIsOwned(true)),
            valorSinger,
            axgardBraggart.copy(isOwned = CardIsOwned(false))
        )

        // WHEN
        val resultActions = mockMvc.perform(get("/sets/afr/cards"))

        // THEN
        resultActions.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("sets/cardsResponse.json"), STRICT)
        )
    }

    @Test
    fun should_return_404_from_unknown_set() {
        // GIVEN
        givenSets(afr)

        // WHEN
        val resultActions = mockMvc.perform(get("/sets/fake/cards"))

        // THEN
        resultActions.andExpectAll(
            status().isNotFound,
        )
    }

}
