package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.Set
import org.junit.jupiter.api.BeforeEach

abstract class AbstractIT(
    val jdbc: JdbcUtils,
) {

    @BeforeEach
    fun setUp() = jdbc.dropAll()

    fun givenSets(vararg set: Set) {
        jdbc.saveSets(*set)
    }

    fun givenCards(vararg card: Card) {
        jdbc.saveCards(*card)
    }

    fun givenEvents(vararg event: Event<*, *, *>) {
        jdbc.saveEvents(*event)
    }
}
