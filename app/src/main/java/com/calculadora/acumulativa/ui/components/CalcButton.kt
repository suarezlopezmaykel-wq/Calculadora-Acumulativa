package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class GlassButtonStyle {
    NUMBER, OPERATOR_BLUE, OPERATOR_GREEN, EQUALS_BLUE, EQUALS_GREEN, SAVE, EXIT
}

@Composable
fun CalcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlassButtonStyle = GlassButtonStyle.NUMBER,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    fontSize: TextUnit = 28.sp,
    height: Dp = 42.dp,
    enabled: Boolean = true
) {
    val textColor = when (style) {
        GlassButtonStyle.NUMBER -> Color(0xFF39FF14)
        GlassButtonStyle.OPERATOR_BLUE, GlassButtonStyle.OPERATOR_GREEN, GlassButtonStyle.EQUALS_BLUE, GlassButtonStyle.EQUALS_GREEN -> Color(0xFF80D8FF)
        else -> Color(0xFFCE93D8)
    }
    Box(
        modifier = modifier
            .height(height)
            .background(Color(0xFF666666), RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) textColor else textColor.copy(alpha = 0.35f),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
