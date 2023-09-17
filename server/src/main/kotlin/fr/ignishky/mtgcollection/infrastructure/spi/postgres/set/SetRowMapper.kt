package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set

import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate

class SetRowMapper : RowMapper<Set> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = Set(
        SetId(rs.getString("id")),
        SetCode(rs.getString("code")),
        SetName(rs.getString("name")),
        SetType(rs.getString("type")),
        SetIcon(rs.getString("icon")),
        SetReleasedAt(LocalDate.parse(rs.getString("released_at"))),
    )

}
