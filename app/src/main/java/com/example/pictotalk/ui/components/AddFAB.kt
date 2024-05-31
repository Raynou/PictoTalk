package com.example.pictotalk.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pictotalk.ui.theme.CristalLake

@Composable
fun AddFAB(onClick: () -> Unit = {}) {
    //val color = Color(0xFFFEF7FF)
    //val color = Color(0xFFFFD8E4);
    val color = CristalLake
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = color,
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }
}