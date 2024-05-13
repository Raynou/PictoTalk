package com.example.pictotalk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun FullScreenFeedback(isCorrect: Boolean, onFeedbackDisplayed: () -> Unit) {
    val backgroundColor = if (isCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
    val textColor = if (isCorrect) Color(0xFF388E3C) else Color(0xFFD32F2F)

    LaunchedEffect(Unit) {
        delay(1000)
        onFeedbackDisplayed()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isCorrect) "¡Correcto!" else "Sigue intentando!",
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFullScreenFeedback() {
    FullScreenFeedback(isCorrect = true, onFeedbackDisplayed = {})
}
