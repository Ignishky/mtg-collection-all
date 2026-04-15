package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card
fun interface CardRefererPort {

    fun getAllCards(): List<Card>

}
