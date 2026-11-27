package com.calculadora.acumulativa.domain

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object CalculatorEngine {

    private val divisionContext = MathContext.DECIMAL128

    // Kept for compatibility with existing callers and tests.
    fun formatNumber(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        return formatDecimal(BigDecimal.valueOf(value))
    }

    private fun formatDecimal(value: BigDecimal): String {
        val normalized = value.stripTrailingZeros()
        return if (normalized.compareTo(BigDecimal.ZERO) == 0) "0" else normalized.toPlainString()
    }

    /**
     * Adds thousands separators for display only. The underlying expression remains
     * unformatted so evaluation and saved calculations continue to use plain numbers.
     */
    fun formatExpressionForDisplay(expression: String): String {
        val numberPattern = Regex("(?<![\\d.])\\d+(?:\\.\\d*)?")
        return numberPattern.replace(expression) { match ->
            val number = match.value
            val parts = number.split('.', limit = 2)
            val groupedInteger = parts[0].reversed().chunked(3).joinToString(",").reversed()
            if (parts.size == 2) "$groupedInteger.${parts[1]}" else groupedInteger
        }
    }

    fun formatNumberForDisplay(number: String): String = formatExpressionForDisplay(number)

    fun isOperator(char: Char): Boolean {
        return char == '+' || char == '−' || char == '-' || char == '×' || char == '*' || char == '÷' || char == '/'
    }

    fun normalizeOperator(char: Char): Char {
        return when (char) {
            '-' -> '−'
            '*' -> '×'
            '/' -> '÷'
            else -> char
        }
    }

    /**
     * Tokenizes a mathematical expression into numbers and operators.
     * Supports negative numbers if at start or after an operator.
     */
    fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var i = 0
        val clean = expression.trim()

        while (i < clean.length) {
            val c = clean[i]
            if (c.isWhitespace()) {
                i++
                continue
            }

            if (isOperator(c)) {
                val isMinus = (c == '-' || c == '−')
                val isUnary = isMinus && (tokens.isEmpty() || isOperatorToken(tokens.last())) && sb.isEmpty()

                if (isUnary) {
                    sb.append('-')
                } else {
                    if (sb.isNotEmpty()) {
                        tokens.add(sb.toString())
                        sb.clear()
                    }
                    tokens.add(normalizeOperator(c).toString())
                }
            } else if (c.isDigit() || c == '.') {
                sb.append(c)
            }
            i++
        }
        if (sb.isNotEmpty()) tokens.add(sb.toString())
        return tokens
    }

    private fun isOperatorToken(token: String): Boolean =
        token.length == 1 && isOperator(token[0])

    /**
     * Exact decimal evaluator. Addition, subtraction and multiplication retain all
     * entered digits; division uses DECIMAL128 precision for repeating decimals.
     */
    fun evaluateDecimal(expression: String): BigDecimal? {
        if (expression.any { !it.isWhitespace() && !it.isDigit() && it != '.' && !isOperator(it) }) return null

        val tokens = tokenize(expression)
        if (tokens.isEmpty() || isOperatorToken(tokens.last())) return null

        val numbers = mutableListOf<BigDecimal>()
        val operators = mutableListOf<Char>()

        for (token in tokens) {
            if (isOperatorToken(token)) {
                operators.add(token[0])
            } else {
                numbers.add(token.toBigDecimalOrNull() ?: return null)
            }
        }

        if (numbers.size != operators.size + 1) return null

        var i = 0
        while (i < operators.size) {
            val op = operators[i]
            if (op == '×' || op == '÷') {
                val left = numbers[i]
                val right = numbers[i + 1]
                if (op == '÷' && right.compareTo(BigDecimal.ZERO) == 0) return null
                val result = try {
                    if (op == '×') left.multiply(right) else left.divide(right, divisionContext)
                } catch (_: ArithmeticException) {
                    return null
                }
                numbers[i] = result
                numbers.removeAt(i + 1)
                operators.removeAt(i)
            } else {
                i++
            }
        }

        var result = numbers[0]
        for (j in operators.indices) {
            val next = numbers[j + 1]
            result = if (operators[j] == '+') result.add(next) else result.subtract(next)
        }
        return result
    }

    // Legacy Double API retained for compatibility; display and app calculations
    // use evaluateDecimal/evaluateFormatted to avoid precision loss.
    fun evaluate(expression: String): Double? {
        val result = evaluateDecimal(expression) ?: return null
        val asDouble = result.toDouble()
        return asDouble.takeIf { it.isFinite() }
    }

    fun evaluateFormatted(expression: String): String? {
        val result = evaluateDecimal(expression) ?: return null
        return formatDecimal(result)
    }
}
