package com.calculadora.acumulativa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Keep all controls clear of Android's status and navigation bars.
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            TopCalculatorPanel(
                state = topState,
                accumulatorState = bottomState,
                onDigitClick = { viewModel.onTopDigitClick(it) },
                onOperatorClick = { viewModel.onTopOperatorClick(it) },
                onDotClick = { viewModel.onTopDotClick() },
                onSquareRootClick = { viewModel.onTopSquareRootClick() },
                onPercentClick = { viewModel.onTopPercentClick() },
                onEqualsClick = { viewModel.onTopEqualsClick() },
                onShortDeleteClick = { viewModel.onTopShortDeleteClick() },
                onResetAllClick = { viewModel.onResetAll() },
                onSaveResultClick = { viewModel.onSaveTopResultToBottom(it) },
                onExitClick = { viewModel.onRedExitClick(onExitApp) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
