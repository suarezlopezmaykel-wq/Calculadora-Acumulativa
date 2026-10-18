package com.calculadora.acumulativa.domain

data class BottomCalculatorState(
    val expression: String = "",
    val fixedTotal: String? = null,
    val isFixed: Boolean = false
) {
    /**
     * Provisional / suggested total calculated automatically.
     */
    val suggestedTotal: String?
        get() {
            if (isFixed) return fixedTotal
            if (expression.isBlank()) return null
            return CalculatorEngine.evaluateFormatted(expression)
        }

    fun addSavedResult(result: String): BottomCalculatorState {
        val trimmedRes = result.trim()
        if (trimmedRes.isEmpty() || trimmedRes == "Error") return this

        val trimmedExpr = expression.trim()
        val newExpr = when {
            trimmedExpr.isEmpty() -> trimmedRes
            CalculatorEngine.isOperator(trimmedExpr.last()) -> "$trimmedExpr $trimmedRes"
            else -> "$trimmedExpr + $trimmedRes"
        }

        return copy(
            expression = newExpr,
            isFixed = false,
            fixedTotal = null
        )
    }

    fun applyOperator(op: String): BottomCalculatorState {
        val trimmedExpr = expression.trim()
        if (trimmedExpr.isEmpty()) {
            return copy(expression = "0 $op ", isFixed = false)
        }

        val lastChar = trimmedExpr.last()
        val newExpr = if (CalculatorEngine.isOperator(lastChar)) {
            // Replace pending sign (Sign correction requirement 5)
            trimmedExpr.dropLast(1).trimEnd() + " " + op + " "
        } else {
            "$trimmedExpr $op "
        }

        return copy(
            expression = newExpr,
            isFixed = false
        )
    }

    fun fixTotal(): BottomCalculatorState {
        val res = CalculatorEngine.evaluateFormatted(expression) ?: return this
        if (res == "Error") return this
        return copy(
            fixedTotal = res,
            isFixed = true
        )
    }

    fun clear(): BottomCalculatorState {
        return BottomCalculatorState()
    }
}
