package com.calculadora.acumulativa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CButton(
    modifier: Modifier = Modifier,
    onShortClick: () -> Unit,
    onLongPress1Sec: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var longPressTriggered by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF666666))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        longPressTriggered = false

                        timerJob = coroutineScope.launch {
                            delay(1000L)
                            longPressTriggered = true
                            onLongPress1Sec()
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
        Text(
            text = "C",
            color = Color(0xFFCE93D8),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
