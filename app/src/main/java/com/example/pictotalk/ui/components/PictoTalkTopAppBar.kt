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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.pictotalk.R
import com.example.pictotalk.game.SettingsManager
import com.example.pictotalk.ui.theme.CristalLake
import com.example.pictotalk.ui.theme.Magical
import com.example.pictotalk.ui.theme.MistyAqua
import com.example.pictotalk.ui.theme.SimplyElegant

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
    val backGroundColor = SimplyElegant
    val font = FontFamily(
        Font(R.font.kids, FontWeight.Light),
    )
    TopAppBar(
        title = { Text(title, fontFamily = font) },
        actions = {
            if(canManageSettings){
                IconButton(onClick = { showDialog.value = true }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = Color.Black
                    )
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
        // Add divider
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