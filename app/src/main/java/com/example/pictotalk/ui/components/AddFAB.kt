package com.example.pictotalk.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun AddFAB(handleClick: () -> Unit = {}) {
    FloatingActionButton(onClick = { handleClick() }) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }
}