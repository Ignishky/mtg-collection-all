package fr.ignishky.mtgcollection.domain.card.model

import java.util.UUID.randomUUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CardTest {

    @Test
    fun `Should generate a card`() {
        // GIVEN
        val uuid = randomUUID()

        // WHEN
        val card = Card(CardId(uuid), CardName("Test card name"))

        // THEN
        assertEquals(uuid, card.id.value)
        assertEquals("Test card name", card.name.value)
    }
}
