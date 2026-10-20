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
        get() = savedItems.joinToString("+")

    /**
     * Formatted expression with thousands separators for display.
     * Example: "1.000+3.000+90" or "0.3+59.97"
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
                val bd = item.toBigDecimalOrNull() ?: BigDecimal.ZERO
                sum += bd
            }
            return sum
        }

    /**
     * Formatted total string.
     * Example: "Total=490" or "Total=1.234.56"
     */
    val formattedTotal: String
        get() = "Total=" + CalculatorEngine.formatNumberString(totalBigDecimal.toPlainString())

    fun addSavedResult(result: String): BottomCalculatorState {
        val trimmedRes = result.trim()
        if (trimmedRes.isEmpty() || trimmedRes == "Error") return this

        // Save raw exact number string without thousand dots
        val newItems = savedItems + trimmedRes

        return copy(savedItems = newItems)
    }

    fun clear(): BottomCalculatorState {
        return BottomCalculatorState()
    }
}
