package com.calculadora.acumulativa.ui

import androidx.lifecycle.ViewModel
import com.calculadora.acumulativa.domain.BottomCalculatorState
import com.calculadora.acumulativa.domain.TopCalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val _topState = MutableStateFlow(TopCalculatorState())
    val topState: StateFlow<TopCalculatorState> = _topState.asStateFlow()

    private val _bottomState = MutableStateFlow(BottomCalculatorState())
    val bottomState: StateFlow<BottomCalculatorState> = _bottomState.asStateFlow()

    // Top Calculator Actions
    fun onTopDigitClick(digit: String) {
        _topState.update { it.appendDigit(digit) }
    }

    fun onTopCommaClick() {
        _topState.update { it.appendComma() }
    }

    fun onTopDotClick() {
        _topState.update { it.appendDot() }
    }

    fun onTopOperatorClick(op: String) {
        _topState.update { it.appendOperator(op) }
    }

    fun onTopPercentClick() {
        _topState.update { it.appendPercent() }
    }

    fun onTopSquareRootClick() {
        _topState.update { it.appendSquareRoot() }
    }

    fun onTopEqualsClick() {
        _topState.update { it.fixResult() }
    }

    fun onTopShortDeleteClick() {
        _topState.update { it.deleteLast() }
    }

    fun onResetAll() {
        // Hold C button for 1 second clears both screens and resets accumulator
        _topState.update { TopCalculatorState() }
        _bottomState.update { BottomCalculatorState() }
    }

    fun onRedExitClick(onCloseActivity: () -> Unit) {
        // Red button: Immediately reset all calculations and states, then close application
        _topState.update { TopCalculatorState() }
        _bottomState.update { BottomCalculatorState() }
        onCloseActivity()
    }

    fun onSaveTopResultToBottom(resultVal: String) {
        if (resultVal.isBlank() || resultVal == "Error") return

        // Send fixed result to lower accumulator
        _bottomState.update { it.addSavedResult(resultVal) }

        // Clear top calculator screen completely, ready for next operation
        _topState.update { TopCalculatorState() }
    }

    // Bottom Calculator Actions (Kept for compatibility)
    fun onBottomOperatorClick(op: String) {
        // Lower manual operators removed
    }

    fun onBottomEqualsClick() {
        // Lower equals button removed
    }
}
