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

    // Test 19: Accumulator format and decimal saving for numbers >= 1000
    @Test
    fun test19_AccumulatorDecimalSavingAndSum() {
        // Test saving integers: 100, 300, 90
        viewModel.onSaveTopResultToBottom("100")
        viewModel.onSaveTopResultToBottom("300")
        viewModel.onSaveTopResultToBottom("90")

        assertEquals("100+300+90", viewModel.bottomState.value.expression)
        assertEquals("Total=490", viewModel.bottomState.value.formattedTotal)

        // Reset and test saving numbers >= 1000 with decimals: 1.234,56 + 1.000,50 -> Total=2.235,06
        viewModel.onResetAll()
        viewModel.onSaveTopResultToBottom("1.234,56")
        viewModel.onSaveTopResultToBottom("1.000,50")

        assertEquals("1.234,56+1.000,50", viewModel.bottomState.value.formattedExpression)
        assertEquals("Total=2.235,06", viewModel.bottomState.value.formattedTotal)

        // Test saving decimals with 3 digits e.g. 1,123 + 2,456 -> Total=3,579
        viewModel.onResetAll()
        viewModel.onSaveTopResultToBottom("1,123")
        viewModel.onSaveTopResultToBottom("2,456")

        assertEquals("1,123+2,456", viewModel.bottomState.value.formattedExpression)
        assertEquals("Total=3,579", viewModel.bottomState.value.formattedTotal)
    }
}
