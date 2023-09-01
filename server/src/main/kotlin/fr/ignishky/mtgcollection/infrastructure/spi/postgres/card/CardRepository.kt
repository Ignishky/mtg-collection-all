package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity
import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

@Named
interface CardRepository : JpaRepository<CardEntity, String> {

    @Query("SELECT c FROM CardEntity c WHERE c.setCode = :code")
    fun findBySet(code: String): List<CardEntity>

}
