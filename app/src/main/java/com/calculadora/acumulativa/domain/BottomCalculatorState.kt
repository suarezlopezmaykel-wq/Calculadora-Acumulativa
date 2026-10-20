package com.calculadora.acumulativa.domain

import java.math.BigDecimal

data class BottomCalculatorState(
    val savedItems: List<String> = emptyList()
) {
    /**
     * Expression string formatted with '+' signs between saved items for internal logic.
     * Example: "100+300+90" or "0.1+0.2"
     */
    val expression: String
        get() = savedItems.joinToString("+") { item ->
            CalculatorEngine.formatNumberString(item)
        }

    /**
     * Formatted expression with thousands separators and Spanish decimal comma for display.
     * Example: "1.000+3.000+90" or "0,1+0,2"
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
     * Example: "Total=490" or "Total=1.234,56"
     */
    val formattedTotal: String
        get() = "Total=" + CalculatorEngine.formatBigDecimal(totalBigDecimal)

    fun addSavedResult(result: String): BottomCalculatorState {
        val trimmedRes = result.trim()
        if (trimmedRes.isEmpty() || trimmedRes == "Error") return this

        // Safely unformat result to preserve exact decimal points
        val rawNum = CalculatorEngine.unformatNumberString(trimmedRes)
        val newItems = savedItems + rawNum

        return copy(savedItems = newItems)
    }

    fun clear(): BottomCalculatorState {
        return BottomCalculatorState()
    }
}
