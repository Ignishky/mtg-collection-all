package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.mtgcollection.domain.SetFixtures.aafr
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.SetFixtures.pafr
import fr.ignishky.mtgcollection.domain.SetFixtures.tkhm
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
internal class SetsFetcherApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    @Test
    fun should_return_empty_sets() {
        // WHEN
        val resultActions = mockMvc.perform(get("/sets"))

        // THEN
        resultActions.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("sets/emptySetsResponse.json"), STRICT)
        )
    }

    @Test
    fun should_return_existing_sets() {
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

}
