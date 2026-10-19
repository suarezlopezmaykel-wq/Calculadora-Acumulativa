package com.calculadora.acumulativa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.calculadora.acumulativa.ui.components.BottomCalculatorPanel
import com.calculadora.acumulativa.ui.components.TopCalculatorPanel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onExitApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topState by viewModel.topState.collectAsState()
    val bottomState by viewModel.bottomState.collectAsState()

    // Deep Navy / Black-Blue Background with Subtle Gradient Glows
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF090D16), // Darkest blue-black top
            Color(0xFF0F172A), // Deep navy blue center
            Color(0xFF070A10)  // Deep black bottom
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val isWideScreen = maxWidth > 600.dp

            if (isWideScreen) {
                // Tablet / Landscape side-by-side layout
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TopCalculatorPanel(
                        state = topState,
                        onDigitClick = { viewModel.onTopDigitClick(it) },
                        onOperatorClick = { viewModel.onTopOperatorClick(it) },
                        onDotClick = { viewModel.onTopDotClick() },
                        onEqualsClick = { viewModel.onTopEqualsClick() },
                        onShortDeleteClick = { viewModel.onTopShortDeleteClick() },
                        onResetAllClick = { viewModel.onResetAll() },
                        onSaveResultClick = { viewModel.onSaveTopResultToBottom(it) },
                        onExitClick = {
                            viewModel.onRedExitClick(onExitApp)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    BottomCalculatorPanel(
                        state = bottomState,
                        onOperatorClick = { viewModel.onBottomOperatorClick(it) },
                        onEqualsClick = { viewModel.onBottomEqualsClick() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            } else {
                // Phone portrait layout with vertical scroll for small devices
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    TopCalculatorPanel(
                        state = topState,
                        onDigitClick = { viewModel.onTopDigitClick(it) },
                        onOperatorClick = { viewModel.onTopOperatorClick(it) },
                        onDotClick = { viewModel.onTopDotClick() },
                        onEqualsClick = { viewModel.onTopEqualsClick() },
                        onShortDeleteClick = { viewModel.onTopShortDeleteClick() },
                        onResetAllClick = { viewModel.onResetAll() },
                        onSaveResultClick = { viewModel.onSaveTopResultToBottom(it) },
                        onExitClick = {
                            viewModel.onRedExitClick(onExitApp)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BottomCalculatorPanel(
                        state = bottomState,
                        onOperatorClick = { viewModel.onBottomOperatorClick(it) },
                        onEqualsClick = { viewModel.onBottomEqualsClick() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
