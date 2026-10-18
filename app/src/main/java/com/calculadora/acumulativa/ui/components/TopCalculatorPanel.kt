package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculadora.acumulativa.domain.TopCalculatorState

@Composable
fun TopCalculatorPanel(
    state: TopCalculatorState,
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onDotClick: () -> Unit,
    onEqualsClick: () -> Unit,
    onShortDeleteClick: () -> Unit,
    onResetAllClick: () -> Unit,
    onSaveResultClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Colors
    val panelBg = Color(0xFF1E3A5F)         // Light blue panel
    val displayBg = Color(0xFFE0F2FE)       // Very light blue display
    val displayBorder = Color(0xFF0F172A)   // Dark border
    val buttonBg = Color(0xFF2563EB)        // Blue buttons
    val buttonBorder = Color(0xFF0F172A)    // Dark borders
    val saveBtnBg = Color(0xFF1D4ED8)       // Darker blue for Save
    val equalsBtnBg = Color(0xFF0284C7)     // Vibrant blue for Equals

    Column(
        modifier = modifier
            .background(panelBg, RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFF0D1B2A), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        // Display Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(displayBg, RoundedCornerShape(8.dp))
                .border(2.dp, displayBorder, RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Expression Line
            Text(
                text = if (state.expression.isBlank()) "0" else state.expression,
                color = Color(0xFF334155),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Result Line
            val resultDisplay = when {
                state.isFixed -> "= ${state.fixedResult} (Fijado)"
                state.suggestedResult != null -> "= ${state.suggestedResult}"
                else -> ""
            }

            Text(
                text = resultDisplay,
                color = if (state.isFixed) Color(0xFF1E40AF) else Color(0xFF0F172A),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Keypad Grid
        // Row 1: 7, 8, 9, ÷, C
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("7", { onDigitClick("7") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("8", { onDigitClick("8") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("9", { onDigitClick("9") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("÷", { onOperatorClick("÷") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CButton(
                modifier = Modifier.weight(1f),
                onShortClick = onShortDeleteClick,
                onLongPress2Sec = onResetAllClick
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 2: 4, 5, 6, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("4", { onDigitClick("4") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("5", { onDigitClick("5") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("6", { onDigitClick("6") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("×", { onOperatorClick("×") }, Modifier.weight(1f), buttonBg, buttonBorder)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 3: 1, 2, 3, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("1", { onDigitClick("1") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("2", { onDigitClick("2") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("3", { onDigitClick("3") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("−", { onOperatorClick("−") }, Modifier.weight(1f), buttonBg, buttonBorder)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 4: 0, ., +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("0", { onDigitClick("0") }, Modifier.weight(2f), buttonBg, buttonBorder)
            CalcButton(".", { onDotClick() }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("+", { onOperatorClick("+") }, Modifier.weight(1f), buttonBg, buttonBorder)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 5: Equals = (under suggested result) & Guardar cálculo
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
                modifier = Modifier.weight(1f),
                backgroundColor = equalsBtnBg,
                borderColor = buttonBorder,
                enabled = canFix
            )

            CalcButton(
                text = "Guardar cálculo",
                onClick = {
                    if (canSave && targetSaveVal.isNotBlank() && targetSaveVal != "Error") {
                        onSaveResultClick(targetSaveVal)
                    }
                },
                modifier = Modifier.weight(2.5f),
                backgroundColor = saveBtnBg,
                borderColor = buttonBorder,
                fontSize = 16.sp,
                enabled = canSave
            )
        }
    }
}
