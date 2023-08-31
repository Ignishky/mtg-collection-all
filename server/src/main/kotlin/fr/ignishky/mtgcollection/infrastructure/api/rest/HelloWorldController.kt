package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.mtgcollection.domain.card.model.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID

@RestController
internal class HelloWorldController : HelloWorldApi {

    override fun hello(): ResponseEntity<Card> {
        return ok(
            Card(
                CardId(randomUUID().toString()),
                CardName("My Card"),
                CardSetCode(""),
                CardImages(listOf()),
                CardNumber(""),
                CardPrices(Price(0, 0, 0, 0))
            )
        )
    }

}
