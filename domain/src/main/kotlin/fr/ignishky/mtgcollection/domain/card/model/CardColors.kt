package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName
import fr.ignishky.mtgcollection.domain.card.model.CardProperty.PropertyName.COLORS

data class CardColors(
    val value: List<CardColor>,
) : CardProperty {

    override fun propertyName(): PropertyName = COLORS

    override fun propertyValue(): String = value.map { it.name }.joinToString { it }
}

enum class CardColor(val symbol: String) {
    WHITE("W"),
    BLUE("U"),
    BLACK("B"),
    RED("R"),
    GREEN("G"),
    UNCOLOR(""),
    ;

    companion object {
        private val bySymbol: Map<String, CardColor> = entries.associateBy { it.symbol }

        fun fromSymbol(symbol: String): CardColor =
            bySymbol[symbol] ?: throw IllegalArgumentException("Unknown color symbol: $symbol")
    }
}