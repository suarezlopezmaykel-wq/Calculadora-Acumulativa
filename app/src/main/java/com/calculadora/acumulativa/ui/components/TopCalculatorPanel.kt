package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculadora.acumulativa.domain.BottomCalculatorState
import com.calculadora.acumulativa.domain.CalculatorEngine
import com.calculadora.acumulativa.domain.TopCalculatorState

@Composable
fun TopCalculatorPanel(
    state: TopCalculatorState,
    accumulatorState: BottomCalculatorState,
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onDotClick: () -> Unit,
    onEqualsClick: () -> Unit,
    onShortDeleteClick: () -> Unit,
    onResetAllClick: () -> Unit,
    onSaveResultClick: (String) -> Unit,
    onAccumulatorOperatorClick: (String) -> Unit,
    onAccumulatorEqualsClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Let the keypad grow with the usable phone height, while keeping
        // each key large enough to tap comfortably on compact devices.
        val keyHeight = ((maxHeight - 220.dp) / 5f).coerceIn(48.dp, 88.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF666666))
                        .clickable { onExitClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "❌", color = Color.Red, fontSize = 13.sp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = CalculatorEngine.formatExpressionForDisplay(
                        state.expression.ifBlank { "0" }
                    ),
                    color = Color.White,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )

                val resultDisplay = if (state.isFixed) {
                    ""
                } else {
                    state.suggestedResult?.let(CalculatorEngine::formatNumberForDisplay).orEmpty()
                }

                if (resultDisplay.isNotEmpty()) {
                    Text(
                        text = resultDisplay,
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Subtle separator between the current calculation and saved calculations.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(1.dp)
                    .background(Color(0xFF666666))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = CalculatorEngine.formatExpressionForDisplay(
                        accumulatorState.expression.ifBlank { "0" }
                    ),
                    color = Color(0xFFFFFF00),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = (accumulatorState.fixedTotal ?: accumulatorState.suggestedTotal ?: "0")
                        .let(CalculatorEngine::formatNumberForDisplay),
                    color = Color(0xFFFFFF00),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton("1", { onDigitClick("1") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("2", { onDigitClick("2") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("3", { onDigitClick("3") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CButton(
                    modifier = Modifier.weight(1f),
                    onShortClick = onShortDeleteClick,
                    onLongPress1Sec = onResetAllClick,
                    height = keyHeight
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton("4", { onDigitClick("4") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("5", { onDigitClick("5") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("6", { onDigitClick("6") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("×", { onOperatorClick("×") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE, height = keyHeight)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton("7", { onDigitClick("7") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("8", { onDigitClick("8") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("9", { onDigitClick("9") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton("−", { onOperatorClick("−") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE, height = keyHeight)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton("÷", { onOperatorClick("÷") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE, height = keyHeight)
                CalcButton("0", { onDigitClick("0") }, Modifier.weight(1f), GlassButtonStyle.NUMBER, height = keyHeight)
                CalcButton(".", { onDotClick() }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE, height = keyHeight)
                CalcButton("+", { onOperatorClick("+") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE, height = keyHeight)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val canFix = state.suggestedResult != null && !state.isFixed
                val canSave = state.isFixed || state.suggestedResult != null
                val targetSaveVal = state.fixedResult ?: state.suggestedResult ?: ""

                CalcButton(
                    text = "=",
                    onClick = onEqualsClick,
                    modifier = Modifier.weight(1.2f),
                    style = GlassButtonStyle.OPERATOR_BLUE,
                    height = keyHeight,
                    enabled = canFix
                )

                CalcButton(
                    text = "↓",
                    onClick = {
                        if (canSave && targetSaveVal.isNotBlank() && targetSaveVal != "Error") {
                            onSaveResultClick(targetSaveVal)
                        }
                    },
                    modifier = Modifier.weight(2.8f),
                    style = GlassButtonStyle.SAVE,
                    fontSize = 20.sp,
                    height = keyHeight,
                    enabled = canSave
                )
            }
        }
    }
}
