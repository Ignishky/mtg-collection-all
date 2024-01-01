package fr.ignishky.mtgcollection.infrastructure.api.rest.collection

data class OwnedBody(
    val ownedFoil: Boolean,
) {
    @Suppress("unused")
    constructor() : this(false)
}
