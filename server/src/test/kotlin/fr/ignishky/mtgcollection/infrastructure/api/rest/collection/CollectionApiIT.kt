package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CollectionApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcUtils,
) {

    private val plus2Mace = plus2Mace()

    @BeforeEach
    fun setUp() {
        jdbc.dropAll()
        jdbc.save(listOf(afr()), listOf(plus2Mace))
    }

    @Test
    fun `Should add card to the collection`() {
        val results = mockMvc.perform(
            put("/collection/${plus2Mace.id.value}")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": false
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isOk)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace.copy(isOwned = CardIsOwned(true)))
    }

    @Test
    fun `Should add foil card to the collection`() {
        val results = mockMvc.perform(
            put("/collection/${plus2Mace.id.value}")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": true
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isOk)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace.copy(isOwned = CardIsOwned(true), isOwnedFoil = CardIsOwnedFoil(true)))
    }

}
