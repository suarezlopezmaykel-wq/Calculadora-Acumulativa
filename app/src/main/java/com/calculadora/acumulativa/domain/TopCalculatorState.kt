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

    fun appendDigit(digit: String): TopCalculatorState {
        val newExpr = if (isFixed) digit else expression + digit
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
            trimmed.dropLast(1).trimEnd() + " " + op + " "
        } else {
            "$trimmed $op "
        }
        return TopCalculatorState(expression = newExpr, isFixed = false)
    }

    fun deleteLast(): TopCalculatorState {
        if (isFixed) return TopCalculatorState(expression = expression, isFixed = false)
        if (expression.isEmpty()) return this
        val trimmed = expression.trimEnd()
        if (trimmed.isEmpty()) return copy(expression = "")
        val newExpr = trimmed.dropLast(1).trimEnd()
        return copy(expression = newExpr, isFixed = false)
    }

    /**
     * Applies percentage as division by 100 to the current expression.
     * Example: 10 × 80 becomes 10 × 80 ÷ 100 (result 8).
     */
    fun appendPercent(): TopCalculatorState {
        val baseExpr = if (isFixed && fixedResult != null && fixedResult != "Error") {
            fixedResult
        } else if (isFixed) {
            ""
        } else {
            expression
        }
        val trimmed = baseExpr.trimEnd()
        val lastChar = trimmed.lastOrNull() ?: return this
        if (CalculatorEngine.isOperator(lastChar)) return this
        return TopCalculatorState(expression = "$trimmed ÷ 100", isFixed = false)
    }

    /**
     * Applies square root to the last entered operand, preserving any earlier
     * operations. For example, 3 + 9 becomes 3 + 3.
     */
    fun applySquareRootToLastOperand(): TopCalculatorState {
        val baseExpr = if (isFixed && fixedResult != null && fixedResult != "Error") {
            fixedResult
        } else if (isFixed) {
            ""
        } else {
            expression
        }
        val trimmed = baseExpr.trimEnd()
        val lastOperand = CalculatorEngine.tokenize(trimmed).lastOrNull() ?: return this
        if (lastOperand.length == 1 && CalculatorEngine.isOperator(lastOperand[0])) return this
        val root = CalculatorEngine.squareRootFormatted(lastOperand) ?: return this
        val operandStart = trimmed.lastIndexOf(lastOperand)
        if (operandStart < 0) return this
        val newExpression = trimmed.substring(0, operandStart) + root
        return TopCalculatorState(expression = newExpression, isFixed = false)
    }

    fun fixResult(): TopCalculatorState {
        val res = CalculatorEngine.evaluateFormatted(expression) ?: return this
        if (res == "Error") return this
        // Replace the operation with its result; keep the raw value for further calculations/saving.
        return copy(expression = res, fixedResult = res, isFixed = true)
    }

    fun clear(): TopCalculatorState = TopCalculatorState()
}
