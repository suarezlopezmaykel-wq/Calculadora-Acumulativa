package com.calculadora.acumulativa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.calculadora.acumulativa.ui.components.TopCalculatorPanel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onExitApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topState by viewModel.topState.collectAsState()
    val bottomState by viewModel.bottomState.collectAsState()

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF090D16), Color(0xFF0F172A), Color(0xFF070A10))
    )

    Box(
        modifier = modifier.fillMaxSize().background(bgGradient)
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            ) {
                TopCalculatorPanel(
                    state = topState,
                    accumulatorState = bottomState,
                    onDigitClick = { viewModel.onTopDigitClick(it) },
                    onOperatorClick = { viewModel.onTopOperatorClick(it) },
                    onDotClick = { viewModel.onTopDotClick() },
                    onEqualsClick = { viewModel.onTopEqualsClick() },
                    onShortDeleteClick = { viewModel.onTopShortDeleteClick() },
                    onResetAllClick = { viewModel.onResetAll() },
                    onSaveResultClick = { viewModel.onSaveTopResultToBottom(it) },
                    onAccumulatorOperatorClick = { viewModel.onBottomOperatorClick(it) },
                    onAccumulatorEqualsClick = { viewModel.onBottomEqualsClick() },
                    onExitClick = { viewModel.onRedExitClick(onExitApp) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
