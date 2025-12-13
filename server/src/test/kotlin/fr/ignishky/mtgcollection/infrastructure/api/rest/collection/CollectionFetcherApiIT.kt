package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.axgardBraggart
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedNonFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedFoil
import fr.ignishky.mtgcollection.infrastructure.AbstractIT
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.json.JsonCompareMode.STRICT
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CollectionFetcherApiIT(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    @Test
    fun should_return_empty_collection_when_no_card_owned() {
        givenSets(afr, khm)
        givenCards(
            arboreaPegasus,
            plus2Mace,
            axgardBraggart,
        )

        val results = mockMvc.perform(get("/collection"))

        results.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("collection/emptyResponse.json"), STRICT)
        )
    }

    @Test
    fun should_retrieve_owned_cards() {
        givenSets(afr, khm)
        givenCards(
            arboreaPegasus.copy(nbOwnedFoil = CardNbOwnedFoil(1)),
            plus2Mace.copy(nbOwnedNonFoil = CardNbOwnedNonFoil(3)),
            axgardBraggart,
        )

        val results = mockMvc.perform(get("/collection"))

        results.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("collection/cardsResponse.json"), STRICT)
        )
    }

}
