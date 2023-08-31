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
    private val id: Long = 0,
    @Column(name = "aggregate_id")
    private val aggregateId: String,
    @Column(name = "aggregate_name")
    private val aggregateName: String,
    private val label: String,
    private val instant: ZonedDateTime,
    private val payload: String,
    @Column(name = "correlation_id")
    private val correlationId: String,
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
