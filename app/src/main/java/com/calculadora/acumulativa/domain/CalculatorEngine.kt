package com.calculadora.acumulativa.domain

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

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
     * Tokenizes a mathematical expression into numbers, percentages, square roots, and operators.
     * Supports negative numbers if at start, after an operator, or inside square root.
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
                // Check if minus sign is part of a negative number or negative square root argument
                val isMinus = (c == '-' || c == '−')
                val isUnary = isMinus && (tokens.isEmpty() || isOperatorToken(tokens.last())) && (sb.isEmpty() || sb.toString() == "√")

                if (isUnary) {
                    sb.append('-')
                } else {
                    if (sb.isNotEmpty()) {
                        tokens.add(sb.toString())
                        sb.clear()
                    }
                    tokens.add(normalizeOperator(c).toString())
                }
            } else if (c.isDigit() || c == '.' || c == '%' || c == '√') {
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
     * Parses a token into a Double value.
     * Handles square root (√) and percentage (%) tokens.
     */
    private fun parseTokenValue(token: String, leftAccumulatedValue: Double?, prevOperator: Char?): Double? {
        var text = token

        // Check if percentage
        val isPercent = text.endsWith('%')
        if (isPercent) {
            text = text.dropLast(1)
        }

        // Check if square root
        val rawNum = if (text.startsWith('√')) {
            val argStr = text.substring(1)
            val argVal = argStr.toDoubleOrNull() ?: return null
            if (argVal < 0) return null // Negative square root error
            sqrt(argVal)
        } else {
            text.toDoubleOrNull() ?: return null
        }

        if (isPercent) {
            return if (prevOperator == '+' || prevOperator == '−' || prevOperator == '-') {
                if (leftAccumulatedValue != null) {
                    leftAccumulatedValue * (rawNum / 100.0)
                } else {
                    rawNum / 100.0
                }
            } else {
                rawNum / 100.0
            }
        }

        return rawNum
    }

    /**
     * Evaluates a tokenized expression respecting operator precedence (× and ÷ before + and −).
     * Handles %, √, division by zero, and negative square roots safely.
     * Returns null if evaluation fails.
     */
    fun evaluate(expression: String): Double? {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return null

        // If expression ends with an operator, it is incomplete
        if (isOperatorToken(tokens.last())) {
            return null
        }

        // First pass: build token list and evaluate operators with precedence
        val rawNumbers = mutableListOf<String>()
        val operators = mutableListOf<Char>()

        var idx = 0
        while (idx < tokens.size) {
            val token = tokens[idx]
            if (isOperatorToken(token)) {
                operators.add(token[0])
            } else {
                rawNumbers.add(token)
            }
            idx++
        }

        if (rawNumbers.size != operators.size + 1) return null

        // Evaluate numbers with context (left accumulated value for percentage)
        // First, calculate left-to-right values for + / - percentage context
        val parsedValues = mutableListOf<Double>()

        // Helper to evaluate up to index
        var currentAcc: Double? = null
        for (i in rawNumbers.indices) {
            val prevOp = if (i > 0) operators[i - 1] else null
            val valNum = parseTokenValue(rawNumbers[i], currentAcc, prevOp) ?: return null
            parsedValues.add(valNum)

            // Update currentAcc for additive percentage context
            if (i == 0) {
                currentAcc = valNum
            } else {
                when (operators[i - 1]) {
                    '+' -> currentAcc = (currentAcc ?: 0.0) + valNum
                    '−', '-' -> currentAcc = (currentAcc ?: 0.0) - valNum
                    '×' -> currentAcc = (currentAcc ?: 0.0) * valNum
                    '÷' -> {
                        if (valNum == 0.0) return null
                        currentAcc = (currentAcc ?: 0.0) / valNum
                    }
                }
            }
        }

        // First pass for precedence: multiply and divide
        val numbers = parsedValues.toMutableList()
        val ops = operators.toMutableList()

        var i = 0
        while (i < ops.size) {
            val op = ops[i]
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
                ops.removeAt(i)
            } else {
                i++
            }
        }

        // Second pass: add and subtract
        var result = numbers[0]
        for (j in ops.indices) {
            val op = ops[j]
            val next = numbers[j + 1]
            if (op == '+') {
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
