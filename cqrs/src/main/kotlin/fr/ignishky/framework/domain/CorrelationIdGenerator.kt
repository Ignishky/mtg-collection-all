package fr.ignishky.framework.domain

import jakarta.inject.Named
import java.util.UUID.randomUUID

@Named
open class CorrelationIdGenerator {

    open fun generate(): CorrelationId {
        return CorrelationId(randomUUID().toString())
    }

}
