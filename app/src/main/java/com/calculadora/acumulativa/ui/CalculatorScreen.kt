package com.calculadora.acumulativa.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.calculadora.acumulativa.ui.components.BottomCalculatorPanel
import com.calculadora.acumulativa.ui.components.TopCalculatorPanel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val topState by viewModel.topState.collectAsState()
    val bottomState by viewModel.bottomState.collectAsState()
    val context = LocalContext.current

    val appBackground = Color(0xFF0D1B2A)

    val handleExit = {
        viewModel.onRedExitClick {
            val activity = context as? Activity
            activity?.finishAndRemoveTask()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackground)
            .safeDrawingPadding()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val isLandscape = maxWidth > maxHeight && maxWidth > 600.dp

            if (isLandscape) {
                // Side-by-side layout for Landscape / Large Tablets
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    TopCalculatorPanel(
                        state = topState,
                        onDigitClick = viewModel::onTopDigitClick,
                        onOperatorClick = viewModel::onTopOperatorClick,
                        onDotClick = viewModel::onTopDotClick,
                        onEqualsClick = viewModel::onTopEqualsClick,
                        onShortDeleteClick = viewModel::onTopShortDeleteClick,
                        onResetAllClick = viewModel::onResetAll,
                        onSaveResultClick = viewModel::onSaveTopResultToBottom,
                        onExitClick = handleExit,
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    )

                    BottomCalculatorPanel(
                        state = bottomState,
                        onOperatorClick = viewModel::onBottomOperatorClick,
                        onEqualsClick = viewModel::onBottomEqualsClick,
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    )
                }
            } else {
                // Stacked layout for Portrait / Phones
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TopCalculatorPanel(
                        state = topState,
                        onDigitClick = viewModel::onTopDigitClick,
                        onOperatorClick = viewModel::onTopOperatorClick,
                        onDotClick = viewModel::onTopDotClick,
                        onEqualsClick = viewModel::onTopEqualsClick,
                        onShortDeleteClick = viewModel::onTopShortDeleteClick,
                        onResetAllClick = viewModel::onResetAll,
                        onSaveResultClick = viewModel::onSaveTopResultToBottom,
                        onExitClick = handleExit,
                        modifier = Modifier.fillMaxWidth()
                    )

                    BottomCalculatorPanel(
                        state = bottomState,
                        onOperatorClick = viewModel::onBottomOperatorClick,
                        onEqualsClick = viewModel::onBottomEqualsClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
