package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
    onPercentClick: () -> Unit = {},
    onSquareRootClick: () -> Unit = {},
    onShortDeleteClick: () -> Unit,
    onResetAllClick: () -> Unit,
    onSaveResultClick: (String) -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Glassmorphic Blue Theme Colors
    val panelBgGradient = listOf(
        Color(0x351E3A5F), // Translucent deep ice blue
        Color(0x200F172A)
    )
    val panelBorder = Color(0x7060A5FA) // Luminous light blue glow border

    val displayBg = Color(0x400F172A)    // Deep dark display glass
    val displayBorder = Color(0x6038BDF8)

    Column(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0x603B82F6))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(panelBgGradient))
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(panelBorder, panelBorder.copy(alpha = 0.2f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        // Top Header Row with Panel Label & Compact Red Exit Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Calculadora Individual",
                color = Color(0xFFE0F2FE),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            // Compact Glassmorphic Red Exit Button (24.dp)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x90DC2626),
                                Color(0x60991B1B)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF87171),
                                Color(0x40F87171)
                            )
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onExitClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Display Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(displayBg)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(displayBorder, displayBorder.copy(alpha = 0.2f))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Expression Line
            Text(
                text = if (state.expression.isBlank()) "0" else state.expression,
                color = Color(0xFFCBD5E1),
                fontSize = 20.sp,
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
                color = if (state.isFixed) Color(0xFF60A5FA) else Color(0xFF38BDF8),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Keypad Grid
        // Row 1: 1, 2, 3, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalcButton("1", { onDigitClick("1") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("2", { onDigitClick("2") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("3", { onDigitClick("3") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("÷", { onOperatorClick("÷") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE)
        }

        Spacer(modifier = Modifier.height(6.dp))

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

        // Row 4: C, 0, ., +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CButton(
                modifier = Modifier.weight(1f),
                onShortClick = onShortDeleteClick,
                onLongPress2Sec = onResetAllClick
            )
            CalcButton("0", { onDigitClick("0") }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton(".", { onDotClick() }, Modifier.weight(1f), GlassButtonStyle.NUMBER)
            CalcButton("+", { onOperatorClick("+") }, Modifier.weight(1f), GlassButtonStyle.OPERATOR_BLUE)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 5: Exact order: "=" | "%" | "√" | "Guardar cálculo"
        // "=", "%", and "√" match the exact width/weight (1f) of numeric buttons
        // "Guardar cálculo" is scaled down in weight (1.6f) to accommodate all 4 buttons legibly
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
                style = GlassButtonStyle.EQUALS_BLUE,
                enabled = canFix
            )

            CalcButton(
                text = "%",
                onClick = onPercentClick,
                modifier = Modifier.weight(1f),
                style = GlassButtonStyle.OPERATOR_BLUE
            )

            CalcButton(
                text = "√",
                onClick = onSquareRootClick,
                modifier = Modifier.weight(1f),
                style = GlassButtonStyle.OPERATOR_BLUE
            )

            CalcButton(
                text = "Guardar cálculo",
                onClick = {
                    if (canSave && targetSaveVal.isNotBlank() && targetSaveVal != "Error") {
                        onSaveResultClick(targetSaveVal)
                    }
                },
                modifier = Modifier.weight(1.6f),
                style = GlassButtonStyle.SAVE,
                fontSize = 11.sp,
                enabled = canSave
            )
        }
    }
}
