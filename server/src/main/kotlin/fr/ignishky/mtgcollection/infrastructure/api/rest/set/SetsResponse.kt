package fr.ignishky.mtgcollection.infrastructure.api.rest.set

data class SetsResponse(
    val sets: List<SetResponse>,
) {

    data class SetResponse(
        val code: String,
        val name: String,
        val type: String,
        val icon: String,
    )

}
