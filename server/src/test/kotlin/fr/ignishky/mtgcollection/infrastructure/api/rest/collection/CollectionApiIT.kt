package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.axgardBraggart
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.TestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CollectionApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcUtils,
) {

    private val afr = afr()
    private val khm = khm()
    private val plus2Mace = plus2Mace().copy(isOwned = CardIsOwned(true))
    private val arboreaPegasus = arboreaPegasus().copy(isOwned = CardIsOwned(true))
    private val axgardBraggart = axgardBraggart()

    @BeforeEach
    fun setUp() {
        jdbc.dropAll()
    }

    @Test
    fun `Should add card to the collection`() {
        jdbc.save(listOf(afr()), listOf(plus2Mace))

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
        assertThat(jdbc.getCards()).containsOnly(plus2Mace.copy(isOwned = CardIsOwned(true)))
    }

    @Test
    fun `Should add foil card to the collection`() {
        jdbc.save(listOf(afr()), listOf(plus2Mace))

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
    }

    @Test
    fun `Should retrieve owned cards`() {
        jdbc.save(listOf(afr, khm), listOf(arboreaPegasus, plus2Mace, axgardBraggart))

        val results = mockMvc.perform(get("/collection"))

        results.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(TestUtils.readFile("collection/cardsResponse.json"), true)
        )
    }

}
