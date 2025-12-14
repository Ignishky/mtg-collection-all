package fr.ignishky.mtgcollection.infrastructure.spi.postgres.event

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.mtgcollection.domain.card.event.*
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.collection.event.CardDisowned
import fr.ignishky.mtgcollection.domain.collection.event.CardOwned
import fr.ignishky.mtgcollection.domain.collection.event.CardOwnedPayload
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetCreatedPayload
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdatedPayload
import fr.ignishky.mtgcollection.domain.set.model.*
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate

class EventRowMapper(
    private val objectMapper: ObjectMapper,
) : RowMapper<Event<*, *, *>> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = when (val label = rs.getString("label")) {
        "SetCreated" -> toSetCreated(rs)
        "SetUpdated" -> toSetUpdated(rs)
        "CardCreated" -> toCardCreated(rs)
        "CardUpdated" -> toCardUpdated(rs)
        "CardPricesUpdated" -> toCardPricesUpdated(rs)
        "CardOwned" -> toCardOwned(rs)
        "CardDisowned" -> toCardDisowned(rs)
        else -> {
            throw IllegalArgumentException("unexpected event type $label")
        }
    }

    private fun toSetCreated(rs: ResultSet): SetCreated {
        val payload = objectMapper.readValue(rs.getString("payload"), SetCreatedPayload::class.java)
        return SetCreated(
            SetId(rs.getString("aggregate_id")),
            SetCode(payload.code),
            SetName(payload.name),
            SetType(payload.type),
            SetIcon(payload.icon),
            SetReleasedAt(LocalDate.parse(payload.releasedAt))
        )
    }

    private fun toSetUpdated(rs: ResultSet): SetUpdated {
        val payload = objectMapper.readValue(rs.getString("payload"), SetUpdatedPayload::class.java)
        return SetUpdated(
            SetId(rs.getString("aggregate_id")),
            *payload.toProperties().toTypedArray<SetProperty>(),
        )
    }

    private fun toCardCreated(rs: ResultSet): CardCreated {
        val payload = objectMapper.readValue(rs.getString("payload"), CardCreatedPayload::class.java)
        return CardCreated(
            CardId(rs.getString("aggregate_id")),
            CardName(payload.name),
            CardSetCode(payload.setCode),
            CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil)),
            CardImages(payload.images.map { CardImage(it) }),
            CardNumber(payload.collectionNumber),
            CardFinishes(payload.finishes.map { CardFinish(it) }),
        )
    }

    private fun toCardUpdated(rs: ResultSet): CardUpdated {
        val payload = objectMapper.readValue(rs.getString("payload"), CardUpdatedPayload::class.java)
        val properties = payload.properties.map { CardProperty.PropertyName.valueOf(it.key).withValue(it.value) }.toTypedArray()
        return CardUpdated(
            CardId(rs.getString("aggregate_id")),
            *properties,
        )
    }

    private fun toCardPricesUpdated(rs: ResultSet): CardPricesUpdated {
        val payload = objectMapper.readValue(rs.getString("payload"), CardPricesUpdatedPayload::class.java)
        return CardPricesUpdated(
            CardId(rs.getString("aggregate_id")),
            CardPrices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil)),
        )
    }

    private fun toCardOwned(rs: ResultSet): CardOwned {
        val payload = objectMapper.readValue(rs.getString("payload"), CardOwnedPayload::class.java)
        return CardOwned(
            CardId(rs.getString("aggregate_id")),
            payload.ownedFoil,
        )
    }

    private fun toCardDisowned(rs: ResultSet): CardDisowned {
        return CardDisowned(
            CardId(rs.getString("aggregate_id")),
        )
    }

}
