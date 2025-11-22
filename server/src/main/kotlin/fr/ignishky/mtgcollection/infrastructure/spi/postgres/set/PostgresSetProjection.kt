package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetProperty
import fr.ignishky.mtgcollection.domain.set.port.SetProjectionPort
import jakarta.inject.Named
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate

@Named
class PostgresSetProjection(
    private val jdbcTemplate: JdbcTemplate,
) : SetProjectionPort {

    override fun add(set: Set) {
        jdbcTemplate.update(
            "INSERT INTO sets (id, code, name, type, icon, released_at, parent_code) VALUES (?, ?, ?, ?, ?, ?, ?)",
            set.id.value,
            set.code.value,
            set.name.value,
            set.type.value,
            set.icon.value,
            set.releasedAt.value,
            set.parentCode?.value,
        )
    }

    override fun update(setId: SetId, properties: List<SetProperty>) {
        val arguments = properties.map { "${it.propertyName().name.lowercase()}='${it.propertyValue()}'" }.joinToString { it }
        jdbcTemplate.update("UPDATE sets SET $arguments WHERE id=?", setId.value)
    }

    override fun getAll(): List<Set> = jdbcTemplate.query("SELECT * FROM sets ORDER BY released_at DESC", SetRowMapper())

    override fun get(id: SetId) = jdbcTemplate.queryForObject("SELECT * FROM sets WHERE id=?", SetRowMapper(), id.value)!!

    override fun get(setCode: SetCode): Set? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM sets WHERE code=?", SetRowMapper(), setCode.value) ?: return null
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
}
