package fr.ignishky.framework.cqrs.event.spi.postgres

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import java.time.Instant
import java.time.Instant.EPOCH
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.ZonedDateTime.ofInstant

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(columnDefinition = "bigserial")
    val id: Long = 0,
    @Column(name = "aggregate_id")
    val aggregateId: String,
    @Column(name = "aggregate_name")
    val aggregateName: String,
    val label: String,
    val instant: ZonedDateTime,
    val payload: String,
    @Column(name = "correlation_id")
    val correlationId: String,
) {

    constructor() : this(0, "", "", "", ofInstant(EPOCH, UTC), "", "")

    constructor(
        id: Long,
        aggregateId: String,
        aggregateName: String,
        label: String,
        instant: Instant,
        payload: String,
        correlationId: String
    ) : this(id, aggregateId, aggregateName, label, ofInstant(instant, UTC), payload, correlationId)

}
