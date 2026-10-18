package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF2563EB),
    borderColor: Color = Color(0xFF0F172A),
    textColor: Color = Color.White,
    fontSize: TextUnit = 20.sp,
    enabled: Boolean = true,
    borderWidth: Dp = 2.dp
) {
    val effectiveBg = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f)
    val effectiveBorder = if (enabled) borderColor else borderColor.copy(alpha = 0.5f)
    val effectiveText = if (enabled) textColor else textColor.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(effectiveBg)
            .border(borderWidth, effectiveBorder, RoundedCornerShape(8.dp))
            .then(
                if (enabled) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = effectiveText,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
