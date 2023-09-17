package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.mapper

import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity

object SetEntityMapper {

    fun toSetEntity(set: Set) = SetEntity(
        set.id.value,
        set.code.value,
        set.name.value,
        set.type.value,
        set.icon.value,
        set.releasedAt.value,
    )

    fun fromSetEntity(setEntity: SetEntity) = Set(
        SetId(setEntity.id),
        SetCode(setEntity.code),
        SetName(setEntity.name),
        SetType(setEntity.type),
        SetIcon(setEntity.icon),
        SetReleasedAt(setEntity.releasedAt),
    )

}
