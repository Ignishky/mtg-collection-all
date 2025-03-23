package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

@JsonInclude(NON_NULL)
data class SetsResponse(
    val sets: List<SetResponse>,
)

data class SetResponse(
    val code: String,
    val name: String,
    val type: String,
    val icon: String,
)
