package com.calculadora.acumulativa

import com.calculadora.acumulativa.domain.BottomCalculatorState
import com.calculadora.acumulativa.domain.CalculatorEngine
import com.calculadora.acumulativa.domain.TopCalculatorState
import com.calculadora.acumulativa.ui.CalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class CalculatorEngineTest {

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    // Test 1: Operación superior "5 + 7 + 6 − 3 = 15"
    @Test
    fun test1_TopOperationEvaluation() {
        viewModel.onTopDigitClick("5")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("7")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("6")
        viewModel.onTopOperatorClick("−")
        viewModel.onTopDigitClick("3")

        assertEquals("5 + 7 + 6 − 3", viewModel.topState.value.expression.trim())
        assertEquals("15", viewModel.topState.value.suggestedResult)
    }

    // Test 2: Fijación mediante "=" y posterior guardado
    @Test
    fun test2_FixWithEqualsAndSave() {
        viewModel.onTopDigitClick("5")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("7")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("6")
        viewModel.onTopOperatorClick("−")
        viewModel.onTopDigitClick("3")

        // Press '='
        viewModel.onTopEqualsClick()
        assertTrue(viewModel.topState.value.isFixed)
        assertEquals("15", viewModel.topState.value.fixedResult)

        // Save result
        viewModel.onSaveTopResultToBottom("15")

        // Check result saved in bottom panel
        assertEquals("15", viewModel.bottomState.value.expression)
        assertEquals("Total=15", viewModel.bottomState.value.formattedTotal)
    }

    // Test 3: Limpieza de la pantalla superior tras guardar
    @Test
    fun test3_TopScreenCleanedAfterSave() {
        viewModel.onTopDigitClick("1")
        viewModel.onTopDigitClick("5")
        viewModel.onTopEqualsClick()
        viewModel.onSaveTopResultToBottom("15")

        assertEquals("", viewModel.topState.value.expression)
        assertNull(viewModel.topState.value.fixedResult)
        assertFalse(viewModel.topState.value.isFixed)
    }

    // Test 4: Acumulación de varios resultados
    @Test
    fun test4_AccumulateMultipleResults() {
        viewModel.onSaveTopResultToBottom("100")
        viewModel.onSaveTopResultToBottom("300")
        viewModel.onSaveTopResultToBottom("90")

        assertEquals("100+300+90", viewModel.bottomState.value.expression)
        assertEquals("Total=490", viewModel.bottomState.value.formattedTotal)
    }

    // Test 5: Entrada de decimales mediante el botón coma y prevención de duplicados
    @Test
    fun test5_CommaDecimalInputAndDuplicateRejection() {
        viewModel.onTopDigitClick("1")
        viewModel.onTopDigitClick("1")
        viewModel.onTopDigitClick("1")
        viewModel.onTopCommaClick()
        viewModel.onTopDigitClick("5")
        viewModel.onTopDigitClick("0")

        assertEquals("111,50", viewModel.topState.value.formattedExpression)

        // Attempt second comma in same token should be rejected
        viewModel.onTopCommaClick()
        assertEquals("111,50", viewModel.topState.value.formattedExpression)
    }

    // Test 6: Comportamiento del botón punto para agrupación de miles
    @Test
    fun test6_DotButtonGrouping() {
        viewModel.onTopDigitClick("1")
        viewModel.onTopDigitClick("0")
        viewModel.onTopDigitClick("0")
        viewModel.onTopDigitClick("0")

        assertEquals("1.000", viewModel.topState.value.formattedExpression)
    }

    // Test 7: Accumulación de "1.234.567,50+200,25+30,00" -> "Total=1.234.797,75"
    @Test
    fun test7_SpecificAccumulatorExample() {
        viewModel.onSaveTopResultToBottom("1.234.567,50")
        viewModel.onSaveTopResultToBottom("200,25")
        viewModel.onSaveTopResultToBottom("30,00")

        assertEquals("1.234.567,50+200,25+30,00", viewModel.bottomState.value.formattedExpression)
        assertEquals("Total=1.234.797,75", viewModel.bottomState.value.formattedTotal)
    }

    // Test 8: 99999999999999999 + 1 -> 100000000000000000
    @Test
    fun test8_SeventeenNinesPlusOne() {
        val result = CalculatorEngine.evaluateFormatted("99999999999999999 + 1")
        assertEquals("100.000.000.000.000.000", result)
    }

    // Test 9: Toque corto de C: borrar exactamente un elemento
    @Test
    fun test9_ShortDeleteCButton() {
        viewModel.onTopDigitClick("1")
        viewModel.onTopDigitClick("2")
        viewModel.onTopDigitClick("3")
        assertEquals("123", viewModel.topState.value.expression)

        viewModel.onTopShortDeleteClick()
        assertEquals("12", viewModel.topState.value.expression)

        viewModel.onTopShortDeleteClick()
        assertEquals("1", viewModel.topState.value.expression)

        viewModel.onTopShortDeleteClick()
        assertEquals("", viewModel.topState.value.expression)
    }

    // Test 10: Pulsación de C durante 1 segundo: borrar ambas pantallas
    @Test
    fun test10_ResetAllOnLongPressC() {
        viewModel.onTopDigitClick("5")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("5")
        viewModel.onSaveTopResultToBottom("10")

        viewModel.onTopDigitClick("9")

        // Trigger reset all
        viewModel.onResetAll()

        assertEquals("", viewModel.topState.value.expression)
        assertEquals("", viewModel.bottomState.value.expression)
        assertEquals("Total=0", viewModel.bottomState.value.formattedTotal)
    }

    // Test 12: División por cero e inválidos
    @Test
    fun test12_DivisionByZeroAndInvalidInput() {
        val divZero = CalculatorEngine.evaluateBigDecimal("10 ÷ 0")
        assertNull(divZero)

        val incomplete = CalculatorEngine.evaluateBigDecimal("10 + ")
        assertNull(incomplete)

        val formattedZeroDiv = CalculatorEngine.evaluateFormatted("10 ÷ 0")
        assertNull(formattedZeroDiv)
    }

    // Test 13: Ausencia de duplicación accidental de resultados guardados
    @Test
    fun test13_PreventAccidentalDuplicateSave() {
        viewModel.onTopDigitClick("1")
        viewModel.onTopDigitClick("0")
        viewModel.onTopEqualsClick()

        val resultVal = viewModel.topState.value.fixedResult!!
        viewModel.onSaveTopResultToBottom(resultVal)

        // Top screen is now empty. Trying to save empty result does nothing
        viewModel.onSaveTopResultToBottom("")
        assertEquals("10", viewModel.bottomState.value.expression)
    }

    // Test 14: Botón rojo de salida borra todos los estados e invoca cierre
    @Test
    fun test14_RedExitButtonResetsAllStatesAndExecutesCallback() {
        viewModel.onTopDigitClick("8")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("2")
        viewModel.onTopEqualsClick()

        viewModel.onSaveTopResultToBottom("10")

        var callbackCalled = false
        viewModel.onRedExitClick { callbackCalled = true }

        assertTrue(callbackCalled)
        assertEquals("", viewModel.topState.value.expression)
        assertEquals("", viewModel.bottomState.value.expression)
    }

    // Test 17: 17-digit precision test "99999999999999999" -> "99.999.999.999.999.999"
    @Test
    fun test17_SeventeenDigitPrecisionAndThousandsSeparators() {
        val seventeenNines = "99999999999999999"
        for (ch in seventeenNines) {
            viewModel.onTopDigitClick(ch.toString())
        }

        // Verify expression keeps exact 17 digits without turning into 100000000000000000
        assertEquals("99999999999999999", viewModel.topState.value.expression)
        assertEquals("99.999.999.999.999.999", viewModel.topState.value.formattedExpression)
        assertEquals("99.999.999.999.999.999", viewModel.topState.value.suggestedResult)
    }

    // Test 18: Exact BigDecimal Decimal Calculations (Spanish format with dot for thousands, comma for decimals)
    @Test
    fun test18_ExactDecimalCalculations() {
        // "100.20 + 11.30" -> "111,50" (Monetary format with 2 decimals)
        assertEquals("111,50", CalculatorEngine.evaluateFormatted("100.20 + 11.30"))

        // "0.1 + 0.2" -> "0,3"
        assertEquals("0,3", CalculatorEngine.evaluateFormatted("0.1 + 0.2"))

        // "19.99 × 3" -> "59,97"
        assertEquals("59,97", CalculatorEngine.evaluateFormatted("19.99 × 3"))

        // "19.99 × 3 + 4.50" -> "64,47"
        assertEquals("64,47", CalculatorEngine.evaluateFormatted("19.99 × 3 + 4.50"))

        // "2 + 3 × 4" -> "14"
        assertEquals("14", CalculatorEngine.evaluateFormatted("2 + 3 × 4"))

        // "1 ÷ 3 × 3" -> "1"
        assertEquals("1", CalculatorEngine.evaluateFormatted("1 ÷ 3 × 3"))
    }
}
