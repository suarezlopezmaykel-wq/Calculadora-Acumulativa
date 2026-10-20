package com.calculadora.acumulativa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Tablet detection for enlarged button dimensions
            val isTablet = maxWidth > 600.dp
            val btnHeight = if (isTablet) 60.dp else 48.dp
            val btnFontSize = if (isTablet) 24.sp else 20.sp
            val saveBtnFontSize = if (isTablet) 14.sp else 11.sp

            // Enforce vertical stacked layout on all devices (phones & tablets)
            // Top panel ALWAYS above, Bottom accumulator ALWAYS below.
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
                    onPercentClick = { viewModel.onTopPercentClick() },
                    onSquareRootClick = { viewModel.onTopSquareRootClick() },
                    onShortDeleteClick = { viewModel.onTopShortDeleteClick() },
                    onResetAllClick = { viewModel.onResetAll() },
                    onSaveResultClick = { viewModel.onSaveTopResultToBottom(it) },
                    onExitClick = {
                        viewModel.onRedExitClick(onExitApp)
                    },
                    btnHeight = btnHeight,
                    btnFontSize = btnFontSize,
                    saveBtnFontSize = saveBtnFontSize,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                BottomCalculatorPanel(
                    state = bottomState,
                    btnHeight = btnHeight,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
