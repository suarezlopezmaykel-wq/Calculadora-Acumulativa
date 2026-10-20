package com.calculadora.acumulativa.domain

import java.math.BigDecimal

data class BottomCalculatorState(
    val savedItems: List<String> = emptyList()
) {
    /**
     * Expression string formatted with '+' signs between saved items.
     * Preserves visible grouping dots, decimal commas, and trailing zeros.
     * Example: "1.234.567,50+200,25+30,00"
     */
    val expression: String
        get() = savedItems.joinToString("+") { item ->
            CalculatorEngine.formatNumberString(item)
        }

    /**
     * Formatted expression with thousands separators and Spanish decimal comma for display.
     * Example: "1.234.567,50+200,25+30,00"
     */
    val formattedExpression: String
        get() = savedItems.joinToString("+") { item ->
            CalculatorEngine.formatNumberString(item)
        }

    /**
     * Exact decimal total accumulated from saved items.
     */
    val totalBigDecimal: BigDecimal
        get() {
            var sum = BigDecimal.ZERO
            for (item in savedItems) {
                val clean = CalculatorEngine.unformatNumberString(item)
                val bd = clean.toBigDecimalOrNull() ?: BigDecimal.ZERO
                sum += bd
            }
            return sum
        }

    /**
     * Formatted total string.
     * Example: "Total=1.234.797,75"
     */
    val formattedTotal: String
        get() = "Total=" + CalculatorEngine.formatBigDecimal(totalBigDecimal)

    fun addSavedResult(result: String): BottomCalculatorState {
        val trimmedRes = result.trim()
        if (trimmedRes.isEmpty() || trimmedRes == "Error") return this

        val formattedRes = CalculatorEngine.formatNumberString(trimmedRes)
        val newItems = savedItems + formattedRes

        return copy(savedItems = newItems)
    }

    fun clear(): BottomCalculatorState {
        return BottomCalculatorState()
    }
}
