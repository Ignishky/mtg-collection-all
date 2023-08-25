package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.mtgcollection.domain.card.model.Card
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/hello")
interface HelloWorldApi {

    @GetMapping
    fun hello(): ResponseEntity<Card>

}
