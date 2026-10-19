package com.calculadora.acumulativa.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class GlassButtonStyle {
    NUMBER,
    OPERATOR_BLUE,
    OPERATOR_GREEN,
    EQUALS_BLUE,
    EQUALS_GREEN,
    SAVE,
    EXIT
}

@Composable
fun CalcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlassButtonStyle = GlassButtonStyle.NUMBER,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    fontSize: TextUnit = 20.sp,
    height: Dp = 48.dp,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scaleAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1.0f,
        label = "glassBtnAlpha"
    )

    // Base colors per style
    val (baseBg, borderGlow, textColor) = when (style) {
        GlassButtonStyle.NUMBER -> Triple(
            Color(0x2B385278), // Translucent dark slate blue
            Color(0x6060A5FA), // Light blue border
            Color(0xFFF1F5F9)  // Crisp white text
        )
        GlassButtonStyle.OPERATOR_BLUE -> Triple(
            Color(0x401D4ED8), // Deep glass blue
            Color(0x9093C5FD), // Vibrant blue glow
            Color(0xFFE0F2FE)
        )
        GlassButtonStyle.OPERATOR_GREEN -> Triple(
            Color(0x40059669), // Emerald glass green
            Color(0x906EE7B7), // Bright green glow
            Color(0xFFECFDF5)
        )
        GlassButtonStyle.EQUALS_BLUE -> Triple(
            Color(0x600284C7), // Bright ocean glass blue
            Color(0xB038BDF8),
            Color.White
        )
        GlassButtonStyle.EQUALS_GREEN -> Triple(
            Color(0x6010B981), // Vivid emerald
            Color(0xB034D399),
            Color.White
        )
        GlassButtonStyle.SAVE -> Triple(
            Color(0x502563EB),
            Color(0x8060A5FA),
            Color.White
        )
        GlassButtonStyle.EXIT -> Triple(
            Color(0x70DC2626), // Glass red
            Color(0xB0F87171),
            Color.White
        )
    }

    val finalBg = (backgroundColor ?: baseBg).copy(alpha = if (enabled) (backgroundColor?.alpha ?: baseBg.alpha) * scaleAlpha else 0.2f)
    val finalBorder = (borderColor ?: borderGlow).copy(alpha = if (enabled) 0.6f else 0.2f)
    val finalTextColor = if (enabled) textColor else Color.White.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .height(height)
            .shadow(
                elevation = if (isPressed) 1.dp else 4.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black,
                spotColor = borderGlow
            )
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        finalBg.copy(alpha = (finalBg.alpha * 1.3f).coerceAtMost(0.9f)),
                        finalBg.copy(alpha = (finalBg.alpha * 0.7f).coerceAtLeast(0.1f))
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        finalBorder,
                        finalBorder.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner Glossy Reflection Highlight (Top 35%)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (enabled) 0.15f else 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )

        Text(
            text = text,
            color = finalTextColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
