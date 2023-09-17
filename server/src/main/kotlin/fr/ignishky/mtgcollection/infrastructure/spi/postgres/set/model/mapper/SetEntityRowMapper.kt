package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.mapper

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate

class SetEntityRowMapper : RowMapper<SetEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = SetEntity(
        rs.getString("id"),
        rs.getString("code"),
        rs.getString("name"),
        rs.getString("type"),
        rs.getString("icon"),
        LocalDate.parse(rs.getString("released_at")),
    )

}
