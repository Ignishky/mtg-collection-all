package fr.ignishky.mtgcollection

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardName
import java.util.UUID.randomUUID
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `Should generate a card`() {
        // GIVEN
        val uuid = randomUUID()

        // WHEN
        val card = Card(CardId(uuid), CardName("Card Name"))

        // THEN
        assertEquals(uuid, card.id.value)
        assertEquals("Card Name", card.name.value)
    }
}
