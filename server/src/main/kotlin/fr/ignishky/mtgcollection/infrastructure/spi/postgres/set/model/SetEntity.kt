package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDate.EPOCH

@Entity
@Table(name = "sets")
data class SetEntity(
    @Id
    internal val id: String,
    internal val code: String,
    internal val name: String,
    internal val type: String,
    internal val icon: String,
    internal val releasedAt: LocalDate,
) {

    constructor() : this("", "", "", "", "", EPOCH)

}
