package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculadora.acumulativa.domain.BottomCalculatorState

@Composable
fun BottomCalculatorPanel(
    state: BottomCalculatorState,
    btnHeight: Dp = 48.dp,
    btnFontSize: TextUnit = 20.sp,
    modifier: Modifier = Modifier
) {
    // Glassmorphic Green Theme Colors
    val panelBgGradient = listOf(
        Color(0x35065F46), // Translucent deep emerald
        Color(0x20022C22)
    )
    val panelBorder = Color(0x7034D399) // Luminous green glow border

    val displayBg = Color(0x40022C22)    // Dark display glass
    val displayBorder = Color(0x6010B981)

    Column(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0x6010B981))
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
        // Panel Label
        Text(
            text = "Acumulador de Resultados",
            color = Color(0xFFD1FAE5),
            fontSize = if (btnHeight > 50.dp) 18.sp else 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

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
            // Expression Line (Cumulative expression & saved items)
            Text(
                text = if (state.formattedExpression.isBlank()) "0" else state.formattedExpression,
                color = Color(0xFFA7F3D0),
                fontSize = if (btnHeight > 50.dp) 22.sp else 18.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Total Line: exact requirement "Total=..." without spaces or "provisional" or divider line
            Text(
                text = state.formattedTotal,
                color = Color(0xFF34D399),
                fontSize = if (btnHeight > 50.dp) 26.sp else 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
