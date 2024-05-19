package com.example.pictotalk.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.example.pictotalk.game.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictoTalkTopAppBar(
    settingsManager: SettingsManager,
    canManageSettings: Boolean,
    canGoBack: Boolean,
    title: String,
    onGoBack: () -> Unit = {},
) {
    val showDialog = remember { mutableStateOf(false) }
    val backGroundColor = Color(0xFFFEF7FF)
    TopAppBar(
        title = { Text(title) },
        actions = {
            if(canManageSettings){
                IconButton(onClick = { showDialog.value = true }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        },
        navigationIcon = {
            if(canGoBack){
                IconButton(onClick = { onGoBack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        // Add background color
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backGroundColor)
    )

    if(canManageSettings && showDialog.value) {
        val radioGroupState = rememberSaveable { mutableStateOf(settingsManager.getDifficulty()) }
        SettingsDialog(
            showDialog = showDialog,
            settingsManager = settingsManager,
            radioGroupState = radioGroupState
        )
    }
}