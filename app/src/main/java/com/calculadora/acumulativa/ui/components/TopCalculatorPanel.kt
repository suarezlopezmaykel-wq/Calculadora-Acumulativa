package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculadora.acumulativa.domain.BottomCalculatorState
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
    // Flat black calculator surface.
    val panelBg = Color.Black

    Column(
        modifier = modifier
            .background(panelBg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        // Top Header Row with Panel Label & Red Exit Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            // Grey exit key with red cross.
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

        // Main calculator display.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Expression Line
            Text(
                text = if (state.expression.isBlank()) "0" else state.expression,
                color = Color.White,
                fontSize = 17.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Result Line
            val resultDisplay = state.fixedResult ?: state.suggestedResult ?: ""

            Text(
                text = resultDisplay,
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Saved calculations and their automatically computed total, in yellow.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = accumulatorState.expression.ifBlank { "0" },
                color = Color(0xFFFFFF00),
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = accumulatorState.fixedTotal ?: accumulatorState.suggestedTotal ?: "0",
                color = Color(0xFFFFFF00),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Compact 4-column keypad. Uniform Keypad Grid (Numbers 1-9, 0 & Swapped C & ÷)
        // Row 1: 1, 2, 3, C
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("1", { onDigitClick("1") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("2", { onDigitClick("2") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("3", { onDigitClick("3") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CButton(
                modifier = Modifier.weight(1f),
                onShortClick = onShortDeleteClick,
                onLongPress2Sec = onResetAllClick
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Row 2: 4, 5, 6, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("4", { onDigitClick("4") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("5", { onDigitClick("5") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("6", { onDigitClick("6") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("×", { onOperatorClick("×") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 3: 7, 8, 9, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("7", { onDigitClick("7") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("8", { onDigitClick("8") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("9", { onDigitClick("9") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("−", { onOperatorClick("−") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 4: ÷, 0, ., +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("÷", { onOperatorClick("÷") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE)
            CalcButton("0", { onDigitClick("0") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton(".", { onDotClick() }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("+", { onOperatorClick("+") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 5: Equals = (under result) & Guardar cálculo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val canFix = state.suggestedResult != null
            val canSave = state.isFixed || canFix
            val targetSaveVal = state.fixedResult ?: state.suggestedResult ?: ""

            CalcButton(
                text = "=",
                onClick = onEqualsClick,
                modifier = Modifier.weight(1.2f),
                style = GlassButtonStyle.EQUALS_BLUE,
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
                fontSize = 16.sp,
                enabled = canSave
            )
        }

    }
}
