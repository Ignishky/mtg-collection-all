package fr.ignishky.mtgcollection.infrastructure.api.rest.refresh

import fr.ignishky.mtgcollection.domain.CardFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.domain.CardFixtures.axgardBraggart
import fr.ignishky.mtgcollection.domain.CardFixtures.halvar
import fr.ignishky.mtgcollection.domain.CardFixtures.plus2Mace
import fr.ignishky.mtgcollection.domain.CardFixtures.valorSinger
import fr.ignishky.mtgcollection.domain.SetFixtures.afr
import fr.ignishky.mtgcollection.domain.SetFixtures.khm
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardPricesUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.infrastructure.AbstractIT
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.MockServerBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockserver.client.MockServerClient
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@MockServerTest("scryfall.base-url=http://localhost:\${mockServerPort}")
class RefreshApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbcUtils: JdbcUtils,
) : AbstractIT(
    jdbcUtils
) {

    private lateinit var mockServer: MockServerClient

    @Test
    fun should_create_new_set_and_cards() {
        prepareMockServer()

        val resultActions = performCall()

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                toSetCreated(khm),
                toCardCreated(axgardBraggart),
                toCardCreated(halvar),
            )
        assertThat(jdbc.getSets()).containsOnly(khm)
        assertThat(jdbc.getCards()).containsOnly(axgardBraggart, halvar)
    }

    @Test
    fun should_skip_update_on_unmodified_set_and_cards() {
        prepareMockServer()
        givenSets(khm)
        givenCards(axgardBraggart, halvar)
        givenEvents(
            toSetCreated(khm.copy(name = SetName("Old Name"), icon = SetIcon("Old Icon"))),
            toSetUpdated(khm),
            toCardCreated(axgardBraggart.copy(images = CardImages(emptyList()), collectionNumber = CardNumber(""))),
            toCardUpdated(axgardBraggart),
            toCardCreated(halvar),
        )

        val resultActions = performCall()

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                toSetCreated(khm.copy(name = SetName("Old Name"), icon = SetIcon("Old Icon"))),
                toSetUpdated(khm),
                toCardCreated(axgardBraggart.copy(images = CardImages(emptyList()), collectionNumber = CardNumber(""))),
                toCardUpdated(axgardBraggart),
                toCardCreated(halvar),
            )
        assertThat(jdbc.getSets()).containsOnly(khm)
        assertThat(jdbc.getCards()).containsOnly(axgardBraggart, halvar)
    }

    @Test
    fun should_update_modified_set_and_cards() {
        prepareMockServer()
        givenSets(khm.copy(name = SetName("Old Name"), icon = SetIcon("Old Icon")))
        givenCards(
            axgardBraggart.copy(prices = CardPrices(Price(0, 0, 0, 0))),
            halvar.copy(images = CardImages(emptyList()), collectionNumber = CardNumber("")),
        )
        givenEvents(
            toSetCreated(khm.copy(name = SetName("Old Name"), icon = SetIcon("Old Icon"))),
            toCardCreated(axgardBraggart.copy(prices = CardPrices(Price(0, 0, 0, 0)))),
            toCardCreated(halvar.copy(images = CardImages(emptyList()), collectionNumber = CardNumber(""))),
        )

        val resultActions = performCall()

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                toSetCreated(khm.copy(name = SetName("Old Name"), icon = SetIcon("Old Icon"))),
                toCardCreated(axgardBraggart.copy(prices = CardPrices(Price(0, 0, 0, 0)))),
                toCardCreated(halvar.copy(images = CardImages(emptyList()), collectionNumber = CardNumber(""))),
                toSetUpdated(khm),
                toCardPricesUpdated(axgardBraggart),
                toCardUpdated(halvar),
            )
        assertThat(jdbc.getSets()).containsOnly(khm)
        assertThat(jdbc.getCards()).containsOnly(axgardBraggart, halvar)
    }

    @Test
    fun should_handle_multiple_pages_of_cards() {
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets("scryfall_set_afr.json")
        mockServerBuilder.prepareCards("afr", "afr_page2")

        val resultActions = performCall()

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("instant")
            .containsOnly(
                toSetCreated(afr),
                toCardCreated(plus2Mace),
                toCardCreated(arboreaPegasus),
                toCardCreated(valorSinger),
            )
        assertThat(jdbc.getSets()).containsOnly(afr)
        assertThat(jdbc.getCards()).containsOnly(plus2Mace, arboreaPegasus, valorSinger)
    }

    private fun prepareMockServer() {
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets("scryfall_set_khm.json")
        mockServerBuilder.prepareCards("khm")
    }

    private fun performCall() = mockMvc.perform(put("/refresh-all"))

    private fun toSetCreated(set: Set) = SetCreated(set.id, set.code, set.name, set.type, set.icon, set.releasedAt)

    private fun toSetUpdated(set: Set) = SetUpdated(set.id, set.name, set.icon)

    private fun toCardCreated(card: Card) =
        CardCreated(card.id, card.name, card.setCode, card.prices, card.images, card.collectionNumber, card.finishes)

    private fun toCardUpdated(card: Card) = CardUpdated(card.id, card.collectionNumber, card.images)

    private fun toCardPricesUpdated(card: Card) = CardPricesUpdated(card.id, card.prices)

}
