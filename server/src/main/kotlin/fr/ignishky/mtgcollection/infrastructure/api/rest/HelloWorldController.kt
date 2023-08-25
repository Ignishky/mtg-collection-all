package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardName
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID

@RestController
internal class HelloWorldController : HelloWorldApi {

    override fun hello(): ResponseEntity<Card> {
        return ok(Card(CardId(randomUUID()), CardName("My Card")))
    }

}
