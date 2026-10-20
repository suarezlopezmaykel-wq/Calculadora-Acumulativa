package com.calculadora.acumulativa.domain

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

object CalculatorEngine {

    fun formatIntegerString(intStr: String): String {
        if (intStr.isEmpty()) return ""
        val sb = StringBuilder()
        var count = 0
        for (i in intStr.length - 1 downTo 0) {
            val c = intStr[i]
            if (c.isDigit()) {
                if (count > 0 && count % 3 == 0) {
                    sb.append('.')
                }
                sb.append(c)
                count++
            } else {
                sb.append(c)
            }
        }
        return sb.reverse().toString()
    }

    /**
     * Formats a raw number string (e.g. "1234.56" or "99999999999999999"):
     * Uses dot (.) for thousands separator in integer part and dot (.) for decimal point.
     * Example: "1234.56" -> "1.234.56"
     * Example: "1000" -> "1.000"
     * Example: "0.3" -> "0.3"
     * Example: "99999999999999999" -> "99.999.999.999.999.999"
     */
    fun formatNumberString(token: String): String {
        if (token.isBlank()) return token

        var prefix = ""
        var suffix = ""
        var numStr = token

        if (numStr.startsWith("√")) {
            prefix = "√"
            numStr = numStr.substring(1)
        }
        if (numStr.endsWith("%")) {
            suffix = "%"
            numStr = numStr.dropLast(1)
        }

        if (numStr.isEmpty()) return token

        val isNegative = numStr.startsWith("-") || numStr.startsWith("−")
        if (isNegative) {
            numStr = numStr.substring(1)
        }

        val parts = numStr.split(".", limit = 2)
        val intPart = parts[0]
        val decPart = if (parts.size > 1) parts[1] else null

        val formattedInt = formatIntegerString(intPart)

        val signStr = if (isNegative) "-" else ""
        val formattedNum = if (decPart != null) {
            "$signStr$formattedInt.$decPart"
        } else {
            "$signStr$formattedInt"
        }

        return "$prefix$formattedNum$suffix"
    }

    fun formatBigDecimal(bd: BigDecimal): String {
        val scaled = bd.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros()
        val plain = scaled.toPlainString()
        if (plain == "-0") return "0"
        return formatNumberString(plain)
    }

    fun formatNumber(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        return formatBigDecimal(BigDecimal.valueOf(value))
    }

    fun formatExpressionForDisplay(expression: String): String {
        if (expression.isBlank()) return ""
        val tokens = tokenize(expression)
        val sb = StringBuilder()
        for (i in tokens.indices) {
            val token = tokens[i]
            if (isOperatorToken(token)) {
                sb.append(" ").append(token).append(" ")
            } else {
                sb.append(formatNumberString(token))
            }
        }
        return sb.toString().trim()
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

    fun isOperatorToken(token: String): Boolean {
        return token.length == 1 && isOperator(token[0])
    }

    private fun parseTokenValueBigDecimal(token: String, leftAccumulatedValue: BigDecimal?, prevOperator: Char?): BigDecimal? {
        var text = token

        val isPercent = text.endsWith('%')
        if (isPercent) {
            text = text.dropLast(1)
        }

        val rawNum = if (text.startsWith('√')) {
            val argStr = text.substring(1)
            val argVal = argStr.toDoubleOrNull() ?: return null
            if (argVal < 0) return null
            val sqrtVal = sqrt(argVal)
            BigDecimal.valueOf(sqrtVal)
        } else {
            text.toBigDecimalOrNull() ?: return null
        }

        if (isPercent) {
            val percentFactor = rawNum.divide(BigDecimal("100"), 16, RoundingMode.HALF_UP)
            return if (prevOperator == '+' || prevOperator == '−' || prevOperator == '-') {
                if (leftAccumulatedValue != null) {
                    leftAccumulatedValue * percentFactor
                } else {
                    percentFactor
                }
            } else {
                percentFactor
            }
        }

        return rawNum
    }

    fun evaluateBigDecimal(expression: String): BigDecimal? {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return null

        if (isOperatorToken(tokens.last())) {
            return null
        }

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

        val parsedValues = mutableListOf<BigDecimal>()
        var currentAcc: BigDecimal? = null

        for (i in rawNumbers.indices) {
            val prevOp = if (i > 0) operators[i - 1] else null
            val valNum = parseTokenValueBigDecimal(rawNumbers[i], currentAcc, prevOp) ?: return null
            parsedValues.add(valNum)

            if (i == 0) {
                currentAcc = valNum
            } else {
                when (operators[i - 1]) {
                    '+' -> currentAcc = (currentAcc ?: BigDecimal.ZERO) + valNum
                    '−', '-' -> currentAcc = (currentAcc ?: BigDecimal.ZERO) - valNum
                    '×' -> currentAcc = (currentAcc ?: BigDecimal.ZERO) * valNum
                    '÷' -> {
                        if (valNum.compareTo(BigDecimal.ZERO) == 0) return null
                        currentAcc = (currentAcc ?: BigDecimal.ZERO).divide(valNum, 16, RoundingMode.HALF_UP)
                    }
                }
            }
        }

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
                    if (right.compareTo(BigDecimal.ZERO) == 0) return null
                    left.divide(right, 16, RoundingMode.HALF_UP)
                }
                numbers[i] = res
                numbers.removeAt(i + 1)
                ops.removeAt(i)
            } else {
                i++
            }
        }

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

    fun evaluate(expression: String): Double? {
        val bd = evaluateBigDecimal(expression) ?: return null
        return bd.toDouble()
    }

    fun evaluateFormatted(expression: String): String? {
        val bdResult = evaluateBigDecimal(expression) ?: return null
        return bdResult.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
    }
}
