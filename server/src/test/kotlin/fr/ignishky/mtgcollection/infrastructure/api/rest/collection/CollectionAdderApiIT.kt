package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.collection.event.CardOwned
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
class CollectionAdderApiIT(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    @Test
    fun should_add_non_foil_card_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(0),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/add")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": false
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(1),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, false))
    }

    @Test
    fun should_add_non_foil_card_a_second_time_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(1),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/add")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": false
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(2),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, false))
    }

    @Test
    fun should_add_foil_card_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(0),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/add")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": true
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(0),
                nbOwnedFoil = CardNbOwnedFoil(1),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, true))
    }

    @Test
    fun should_add_foil_card_to_the_collection_on_non_foil() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(1),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/add")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": true
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(1),
                nbOwnedFoil = CardNbOwnedFoil(1),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, true))
    }

    @Test
    fun should_add_foil_card_a_second_time_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(0),
                nbOwnedFoil = CardNbOwnedFoil(1),
            )
        )

        val results = mockMvc.perform(
            post("/collection/${plus2Mace.id.value}/add")
                .contentType(APPLICATION_JSON)
                .content(
                    """{
                    |  "ownedFoil": true
                    |}""".trimMargin()
                )
        )

        results.andExpect(status().isNoContent)
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                nbOwnedNonFoil = CardNbOwnedNonFoil(0),
                nbOwnedFoil = CardNbOwnedFoil(2),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, true))
    }

}
