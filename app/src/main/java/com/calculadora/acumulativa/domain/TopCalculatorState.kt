package com.calculadora.acumulativa.domain

data class TopCalculatorState(
    val expression: String = "",
    val fixedResult: String? = null,
    val isFixed: Boolean = false
) {
    /**
     * Computed suggested result based on current expression.
     * Returns null if incomplete or invalid.
     */
    val suggestedResult: String?
        get() {
            if (isFixed) return fixedResult
            if (expression.isBlank()) return null
            return CalculatorEngine.evaluateFormatted(expression)
        }

    val formattedExpression: String
        get() = CalculatorEngine.formatExpressionForDisplay(expression)

    fun appendDigit(digit: String): TopCalculatorState {
        val newExpr = if (isFixed) digit else expression + digit
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun appendComma(): TopCalculatorState {
        val baseExpr = if (isFixed) "" else expression
        val tokens = CalculatorEngine.tokenize(baseExpr)
        val lastToken = tokens.lastOrNull() ?: ""
        if (lastToken.contains(',') || lastToken.contains('.')) return this.copy(isFixed = false)
        val newExpr = if (baseExpr.isEmpty() || CalculatorEngine.isOperator(baseExpr.last())) {
            baseExpr + "0,"
        } else {
            baseExpr + ","
        }
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun appendDot(): TopCalculatorState {
        val baseExpr = if (isFixed) "" else expression
        val tokens = CalculatorEngine.tokenize(baseExpr)
        val lastToken = tokens.lastOrNull() ?: ""
        if (lastToken.contains('.')) return this.copy(isFixed = false)
        val newExpr = if (baseExpr.isEmpty() || CalculatorEngine.isOperator(baseExpr.last())) {
            baseExpr + "0."
        } else {
            baseExpr + "."
        }
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun appendOperator(op: String): TopCalculatorState {
        val baseExpr = if (isFixed && fixedResult != null && fixedResult != "Error") {
            fixedResult
        } else if (isFixed) {
            ""
        } else {
            expression
        }

        if (baseExpr.isBlank()) return this

        val trimmed = baseExpr.trimEnd()
        val lastChar = trimmed.lastOrNull()

        val newExpr = if (lastChar != null && CalculatorEngine.isOperator(lastChar)) {
            // Replace last operator
            trimmed.dropLast(1).trimEnd() + " " + op + " "
        } else {
            "$trimmed $op "
        }
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun appendPercent(): TopCalculatorState {
        val baseExpr = if (isFixed && fixedResult != null && fixedResult != "Error") {
            fixedResult
        } else if (isFixed) {
            ""
        } else {
            expression
        }

        if (baseExpr.isBlank()) return this
        val trimmed = baseExpr.trimEnd()
        if (trimmed.endsWith('%') || CalculatorEngine.isOperator(trimmed.last())) return this

        val newExpr = "$trimmed%"
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun appendSquareRoot(): TopCalculatorState {
        val baseExpr = if (isFixed && fixedResult != null && fixedResult != "Error") {
            "√$fixedResult"
        } else if (isFixed) {
            "√"
        } else {
            expression
        }

        if (baseExpr.isBlank()) {
            return TopCalculatorState(expression = "√", isFixed = false)
        }

        val trimmed = baseExpr.trimEnd()
        val lastChar = trimmed.lastOrNull()

        val newExpr = if (lastChar != null && CalculatorEngine.isOperator(lastChar)) {
            "$trimmed √"
        } else {
            "√$trimmed"
        }
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun deleteLast(): TopCalculatorState {
        if (isFixed) {
            return TopCalculatorState(expression = expression, isFixed = false)
        }
        if (expression.isEmpty()) return this
        val trimmed = expression.trimEnd()
        if (trimmed.isEmpty()) return copy(expression = "")
        val newExpr = trimmed.dropLast(1).trimEnd()
        return copy(expression = newExpr, isFixed = false)
    }

    fun fixResult(): TopCalculatorState {
        val res = CalculatorEngine.evaluateFormatted(expression) ?: return this
        if (res == "Error") return this
        return copy(fixedResult = res, isFixed = true)
    }

    fun clear(): TopCalculatorState {
        return TopCalculatorState()
    }
}
