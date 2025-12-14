package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.collection.event.CardDisowned
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedNonFoil
import fr.ignishky.mtgcollection.infrastructure.AbstractIT
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CollectionRemoverApiIT(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    @Test
    fun should_remove_non_foil_card_from_the_collection() {
        givenSets(afr)
        givenCards(plus2Mace.copy(nbOwnedNonFoil = CardNbOwnedNonFoil(1)))

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/remove")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": false
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardDisowned(plus2Mace.id))
    }

    @Test
    fun should_remove_foil_card_from_the_collection() {
        givenSets(afr)
        givenCards(plus2Mace.copy(nbOwnedFoil = CardNbOwnedFoil(1)))

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/remove")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": true
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardDisowned(plus2Mace.id))
    }

}
