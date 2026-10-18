package com.calculadora.acumulativa.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CButton(
    modifier: Modifier = Modifier,
    onShortClick: () -> Unit,
    onLongPress2Sec: () -> Unit,
    normalBgColor: Color = Color(0xFFEF4444),
    pressedBgColor: Color = Color(0xFF991B1B),
    borderColor: Color = Color(0xFF7F1D1D),
    textColor: Color = Color.White
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHolding2Sec by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val currentBgColor by animateColorAsState(
        targetValue = when {
            isHolding2Sec -> Color(0xFFB91C1C)
            isPressed -> pressedBgColor
            else -> normalBgColor
        },
        label = "CButtonColor"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(currentBgColor)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        isHolding2Sec = false
                        var triggeredLongPress = false
                        val startTime = System.currentTimeMillis()

                        val timerJob = coroutineScope.launch {
                            delay(2000L)
                            triggeredLongPress = true
                            isHolding2Sec = true
                            onLongPress2Sec()
                        }

                        val released = tryAwaitRelease()
                        timerJob.cancel()
                        isPressed = false
                        isHolding2Sec = false

                        if (released && !triggeredLongPress) {
                            val duration = System.currentTimeMillis() - startTime
                            if (duration < 2000L) {
                                onShortClick()
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "C",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
