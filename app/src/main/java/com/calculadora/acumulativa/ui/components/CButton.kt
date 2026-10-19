package com.calculadora.acumulativa.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CButton(
    modifier: Modifier = Modifier,
    onShortClick: () -> Unit,
    onLongPress2Sec: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var longPressTriggered by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    val scaleAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1.0f,
        label = "cBtnAlpha"
    )

    // Glass red aesthetics
    val glassRedBg = Color(0x40EF4444)
    val glassRedBorder = Color(0x90F87171)

    Box(
        modifier = modifier
            .height(48.dp)
            .shadow(
                elevation = if (isPressed) 1.dp else 4.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black,
                spotColor = glassRedBorder
            )
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        glassRedBg.copy(alpha = (glassRedBg.alpha * 1.3f * scaleAlpha).coerceAtMost(0.9f)),
                        glassRedBg.copy(alpha = (glassRedBg.alpha * 0.7f * scaleAlpha).coerceAtLeast(0.1f))
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        glassRedBorder,
                        glassRedBorder.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        longPressTriggered = false

                        timerJob = coroutineScope.launch {
                            delay(2000L)
                            longPressTriggered = true
                            onLongPress2Sec()
                        }

                        tryAwaitRelease()

                        timerJob?.cancel()
                        timerJob = null
                        isPressed = false

                        if (!longPressTriggered) {
                            onShortClick()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Inner Glossy Reflection Highlight
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )

        Text(
            text = "C",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
