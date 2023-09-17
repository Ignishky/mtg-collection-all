package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate

@Named
class SetPostgresStore(
    private val jdbcTemplate: JdbcTemplate,
) : SetStorePort {

    override fun add(set: Set) {
        jdbcTemplate.update(
            "INSERT INTO sets (id, code, name, type, icon, released_at) VALUES (?, ?, ?, ?, ?, ?)",
            set.id.value,
            set.code.value,
            set.name.value,
            set.type.value,
            set.icon.value,
            set.releasedAt.value,
        )
    }

    override fun update(set: Set) {
        jdbcTemplate.update(
            "UPDATE sets SET code=?, name=?, type=?, icon=?, released_at=? WHERE id=?",
            set.code.value,
            set.name.value,
            set.type.value,
            set.icon.value,
            set.releasedAt.value,
            set.id.value
        )
    }

    override fun getAll() = jdbcTemplate.query("SELECT * FROM sets", SetRowMapper())

    override fun get(id: SetId) = jdbcTemplate.queryForObject("SELECT * FROM sets WHERE id=?", SetRowMapper(), id.value)!!

}
