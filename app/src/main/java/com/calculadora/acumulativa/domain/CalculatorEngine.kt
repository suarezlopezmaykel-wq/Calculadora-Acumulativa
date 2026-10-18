package com.calculadora.acumulativa.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CalculatorEngine {

    fun formatNumber(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        val bd = BigDecimal.valueOf(value).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros()
        val plain = bd.toPlainString()
        return if (plain == "-0") "0" else plain
    }

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
                // Check if this minus sign is part of a negative number token
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
        if (sb.isNotEmpty()) {
            tokens.add(sb.toString())
        }
        return tokens
    }

    private fun isOperatorToken(token: String): Boolean {
        return token.length == 1 && isOperator(token[0])
    }

    /**
     * Evaluates a tokenized expression respecting operator precedence (× and ÷ before + and −).
     * Returns null if evaluation fails or division by zero occurs.
     */
    fun evaluate(expression: String): Double? {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return null

        // If expression ends with an operator, it is incomplete
        if (isOperatorToken(tokens.last())) {
            return null
        }

        // Parse numbers and operators
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<Char>()

        var idx = 0
        while (idx < tokens.size) {
            val token = tokens[idx]
            if (isOperatorToken(token)) {
                // Expect operator
                operators.add(token[0])
            } else {
                val num = token.toDoubleOrNull() ?: return null
                numbers.add(num)
            }
            idx++
        }

        if (numbers.size != operators.size + 1) return null

        // First pass: multiply and divide
        var i = 0
        while (i < operators.size) {
            val op = operators[i]
            if (op == '×' || op == '÷') {
                val left = numbers[i]
                val right = numbers[i + 1]
                val res = if (op == '×') {
                    left * right
                } else {
                    if (right == 0.0) return null // Division by zero
                    left / right
                }
                numbers[i] = res
                numbers.removeAt(i + 1)
                operators.removeAt(i)
            } else {
                i++
            }
        }

        // Second pass: add and subtract
        var result = numbers[0]
        for (j in operators.indices) {
            val op = operators[j]
            val next = numbers[j + 1]
            if (op == '+' || op == '+') {
                result += next
            } else if (op == '−' || op == '-') {
                result -= next
            }
        }

        return result
    }

    fun evaluateFormatted(expression: String): String? {
        val valResult = evaluate(expression) ?: return null
        return formatNumber(valResult)
    }
}
