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
import com.calculadora.acumulativa.domain.BottomCalculatorState

@Composable
fun BottomCalculatorPanel(
    state: BottomCalculatorState,
    onOperatorClick: (String) -> Unit,
    onEqualsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Colors
    val panelBg = Color(0xFF20503B)         // Light green panel (brighter green)
    val displayBg = Color(0xFFDCFCE7)       // Very light green display
    val displayBorder = Color(0xFF064E3B)   // Dark green border
    val buttonBg = Color(0xFF059669)        // Green buttons
    val buttonBorder = Color(0xFF064E3B)    // Dark green borders
    val equalsBtnBg = Color(0xFF10B981)     // Bright green for Equals

    Column(
        modifier = modifier
            .background(panelBg, RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFF064E3B), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        // Panel Label
        Text(
            text = "Acumulador de Resultados",
            color = Color(0xFFECFDF5),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Display Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(displayBg, RoundedCornerShape(8.dp))
                .border(2.dp, displayBorder, RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Expression Line (Saved results & pending operators)
            Text(
                text = if (state.expression.isBlank()) "Sin resultados guardados" else state.expression,
                color = Color(0xFF065F46),
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Result Line
            val resultDisplay = when {
                state.isFixed -> "Total Acumulado = ${state.fixedTotal} (Fijado)"
                state.suggestedTotal != null -> "Total Provisional = ${state.suggestedTotal}"
                else -> "Total Provisional = 0"
            }

            Text(
                text = resultDisplay,
                color = if (state.isFixed) Color(0xFF047857) else Color(0xFF064E3B),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Keypad: Operators +, −, ×, ÷ and Equals =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("+", { onOperatorClick("+") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("−", { onOperatorClick("−") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("×", { onOperatorClick("×") }, Modifier.weight(1f), buttonBg, buttonBorder)
            CalcButton("÷", { onOperatorClick("÷") }, Modifier.weight(1f), buttonBg, buttonBorder)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Single '=' button below suggested result to fix total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val canFix = state.suggestedTotal != null && state.expression.isNotBlank()
            CalcButton(
                text = "=",
                onClick = onEqualsClick,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = equalsBtnBg,
                borderColor = buttonBorder,
                enabled = canFix
            )
        }
    }
}
