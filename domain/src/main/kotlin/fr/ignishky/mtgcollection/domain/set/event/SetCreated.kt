package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import java.time.Instant.now

class SetCreated(
    aggregateId: SetId,
    code: SetCode,
    name: SetName,
    type: SetType,
    icon: SetIcon,
    releasedAt: SetReleasedAt,
    nbCards: SetNbCards,
) : Event<SetId, Set, SetCreatedPayload>(
    0,
    aggregateId,
    Set::class,
    SetCreatedPayload(
        code.value,
        name.value,
        type.value,
        icon.value,
        releasedAt.value.toString(),
        nbCards.value,
    ),
    now(),
) {

    constructor(set: Set) : this(set.id, set.code, set.name, set.type, set.icon, set.releasedAt, set.nbCards)

}

data class SetCreatedPayload(
    val code: String,
    val name: String,
    val type: String,
    val icon: String,
    val releasedAt: String,
    val nbCards: Int,
) : Payload {
    @Suppress("unused")
    constructor() : this("", "", "", "", "", 0)
}
