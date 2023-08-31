package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.framework.domain.Aggregate
import java.time.LocalDate.EPOCH

data class Set(
    val id: SetId,
    val code: SetCode,
    val name: SetName,
    val type: SetType,
    val icon: SetIcon,
    val releasedAt: SetReleasedAt,
) : Aggregate<SetId> {

    constructor() : this(SetId(""), SetCode(""), SetName(""), SetType(""), SetIcon(""), SetReleasedAt(EPOCH))

    override fun id(): SetId {
        return id
    }

}
