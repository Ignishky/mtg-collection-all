package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.card.event.CardOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwned
import fr.ignishky.mtgcollection.domain.card.model.CardIsOwnedFoil
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwned
import fr.ignishky.mtgcollection.domain.card.model.CardNbOwnedFoil
import fr.ignishky.mtgcollection.infrastructure.AbstractIT
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
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
                isOwned = CardIsOwned(false),
                nbOwned = CardNbOwned(0),
                isOwnedFoil = CardIsOwnedFoil(false),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

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
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(1),
                isOwnedFoil = CardIsOwnedFoil(false),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(false)))
    }

    @Test
    fun should_add_non_foil_card_a_second_time_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(1),
                isOwnedFoil = CardIsOwnedFoil(false),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

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
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(2),
                isOwnedFoil = CardIsOwnedFoil(false),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(false)))
    }

    @Test
    fun should_add_foil_card_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                isOwned = CardIsOwned(false),
                nbOwned = CardNbOwned(0),
                isOwnedFoil = CardIsOwnedFoil(false),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

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
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(1),
                isOwnedFoil = CardIsOwnedFoil(true),
                nbOwnedFoil = CardNbOwnedFoil(1),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
    }

    @Test
    fun should_add_foil_card_to_the_collection_on_non_foil() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(1),
                isOwnedFoil = CardIsOwnedFoil(false),
                nbOwnedFoil = CardNbOwnedFoil(0),
            )
        )

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
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(2),
                isOwnedFoil = CardIsOwnedFoil(true),
                nbOwnedFoil = CardNbOwnedFoil(1),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
    }

    @Test
    fun should_add_foil_card_a_second_time_to_the_collection() {
        givenSets(afr)
        givenCards(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(1),
                isOwnedFoil = CardIsOwnedFoil(true),
                nbOwnedFoil = CardNbOwnedFoil(1),
            )
        )

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
        assertThat(jdbc.getCards()).containsOnly(
            plus2Mace.copy(
                isOwned = CardIsOwned(true),
                nbOwned = CardNbOwned(2),
                isOwnedFoil = CardIsOwnedFoil(true),
                nbOwnedFoil = CardNbOwnedFoil(2),
            )
        )
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(CardOwned(plus2Mace.id, CardIsOwnedFoil(true)))
    }

}
