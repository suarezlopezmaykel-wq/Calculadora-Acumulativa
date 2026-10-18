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
        assertEquals("15", viewModel.bottomState.value.suggestedTotal)
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
        viewModel.onSaveTopResultToBottom("15")
        viewModel.onBottomOperatorClick("+")
        viewModel.onSaveTopResultToBottom("20")
        viewModel.onBottomOperatorClick("−")
        viewModel.onSaveTopResultToBottom("5")

        assertEquals("15 + 20 − 5", viewModel.bottomState.value.expression.trim())
        assertEquals("30", viewModel.bottomState.value.suggestedTotal)
    }

    // Test 5: Cambio de un signo pendiente por otro
    @Test
    fun test5_SignCorrectionInAccumulator() {
        viewModel.onSaveTopResultToBottom("15")
        viewModel.onBottomOperatorClick("−") // Mistake
        assertEquals("15 −", viewModel.bottomState.value.expression.trim())

        viewModel.onBottomOperatorClick("+") // Correct to '+'
        assertEquals("15 +", viewModel.bottomState.value.expression.trim())

        viewModel.onSaveTopResultToBottom("20")
        assertEquals("15 + 20", viewModel.bottomState.value.expression.trim())
        assertEquals("35", viewModel.bottomState.value.suggestedTotal)
    }

    // Test 6: Actualización del resultado provisional
    @Test
    fun test6_ProvisionalResultAutoUpdate() {
        viewModel.onSaveTopResultToBottom("10")
        assertEquals("10", viewModel.bottomState.value.suggestedTotal)

        viewModel.onBottomOperatorClick("×")
        viewModel.onSaveTopResultToBottom("5")
        assertEquals("50", viewModel.bottomState.value.suggestedTotal)
    }

    // Test 7: Fijación del total mediante el "=" inferior
    @Test
    fun test7_FixTotalWithBottomEquals() {
        viewModel.onSaveTopResultToBottom("15")
        viewModel.onBottomOperatorClick("+")
        viewModel.onSaveTopResultToBottom("20")
        viewModel.onBottomOperatorClick("−")
        viewModel.onSaveTopResultToBottom("5")

        assertFalse(viewModel.bottomState.value.isFixed)
        viewModel.onBottomEqualsClick()

        assertTrue(viewModel.bottomState.value.isFixed)
        assertEquals("30", viewModel.bottomState.value.fixedTotal)
    }

    // Test 8: Continuación de operaciones a partir de un total fijado
    @Test
    fun test8_ContinueFromFixedTotal() {
        viewModel.onSaveTopResultToBottom("15")
        viewModel.onBottomOperatorClick("+")
        viewModel.onSaveTopResultToBottom("15")
        viewModel.onBottomEqualsClick()
        assertEquals("30", viewModel.bottomState.value.fixedTotal)

        // Continue by adding another operator
        viewModel.onBottomOperatorClick("÷")
        viewModel.onSaveTopResultToBottom("3")

        assertEquals("15 + 15 ÷ 3", viewModel.bottomState.value.expression.trim())
        assertEquals("20", viewModel.bottomState.value.suggestedTotal)
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

    // Test 10: Pulsación de C durante 2 segundos: borrar ambas pantallas
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
        assertNull(viewModel.bottomState.value.suggestedTotal)
    }

    // Test 12: División por cero e inválidos
    @Test
    fun test12_DivisionByZeroAndInvalidInput() {
        val divZero = CalculatorEngine.evaluate("10 ÷ 0")
        assertNull(divZero)

        val incomplete = CalculatorEngine.evaluate("10 + ")
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
        // Populate top calculator
        viewModel.onTopDigitClick("8")
        viewModel.onTopOperatorClick("+")
        viewModel.onTopDigitClick("2")
        viewModel.onTopEqualsClick()

        // Populate bottom accumulator
        viewModel.onSaveTopResultToBottom("10")
        viewModel.onBottomOperatorClick("×")
        viewModel.onSaveTopResultToBottom("5")
        viewModel.onBottomEqualsClick()

        // Verify active calculation states before exit
        assertEquals("50", viewModel.bottomState.value.fixedTotal)
        assertTrue(viewModel.bottomState.value.isFixed)

        var callbackCalled = false
        viewModel.onRedExitClick { callbackCalled = true }

        // Assert callback invoked and states completely reset
        assertTrue(callbackCalled)
        assertEquals("", viewModel.topState.value.expression)
        assertNull(viewModel.topState.value.fixedResult)
        assertFalse(viewModel.topState.value.isFixed)

        assertEquals("", viewModel.bottomState.value.expression)
        assertNull(viewModel.bottomState.value.fixedTotal)
        assertFalse(viewModel.bottomState.value.isFixed)
        assertNull(viewModel.bottomState.value.suggestedTotal)
    }

    // Test 15: Comprobar preservación del funcionamiento del botón C
    @Test
    fun test15_CButtonBehaviorPreserved() {
        viewModel.onTopDigitClick("9")
        viewModel.onTopDigitClick("5")
        viewModel.onTopShortDeleteClick()
        assertEquals("9", viewModel.topState.value.expression)

        viewModel.onSaveTopResultToBottom("9")
        viewModel.onTopDigitClick("4")
        viewModel.onResetAll() // 2 sec hold logic
        assertEquals("", viewModel.topState.value.expression)
        assertEquals("", viewModel.bottomState.value.expression)
    }
}
