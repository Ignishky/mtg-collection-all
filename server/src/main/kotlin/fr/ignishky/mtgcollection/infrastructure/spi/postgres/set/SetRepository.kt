package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity
import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface SetRepository : JpaRepository<SetEntity, String>
