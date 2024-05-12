package com.example.pictotalk

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pictotalk.game.Difficulty
import com.example.pictotalk.game.SettingsManager
import com.example.pictotalk.ui.GameScreen
import com.example.pictotalk.ui.MainMenuScreen

enum class PictoTalkScreen(@StringRes val title: Int? = null) {
    Start(title = R.string.app_name),
    Game
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictoTalkTopAppBar(settingsManager: SettingsManager) {
    val showDialog = remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("PictoTalk") },
        actions = {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )

    if (showDialog.value) {
        val radioGroupState = rememberSaveable { mutableStateOf(settingsManager.getDifficulty()) }
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Dificultad") },
            text = {
                Column {
                    Text("Fácil: para niños de 2 a 5 años")
                    Text("Medio: para niños de 5 a 7 años")
                    Text("Difícil: 9 años en adelante")

                    // Espacio en blanco
                    Text("\n")

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Fácil")
                        RadioButton(
                            selected = radioGroupState.value == Difficulty.EASY,
                            onClick = { radioGroupState.value = Difficulty.EASY }
                        )
                    }

                    Divider()

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Medio")
                        RadioButton(
                            selected = radioGroupState.value == Difficulty.MEDIUM,
                            onClick = { radioGroupState.value = Difficulty.MEDIUM }
                        )
                    }

                    Divider()
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Difícil")
                        RadioButton(
                            selected = radioGroupState.value == Difficulty.HARD,
                            onClick = { radioGroupState.value = Difficulty.HARD }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    settingsManager.setDifficulty(radioGroupState.value)
                    showDialog.value = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun PictoTalkApp(
    navController: NavHostController = rememberNavController()
) {
    val backStateEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PictoTalkScreen.valueOf(
        backStateEntry?.destination?.route ?: PictoTalkScreen.Start.name
    )
    Scaffold {  innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PictoTalkScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(PictoTalkScreen.Start.name) {
                MainMenuScreen(
                    topAppBar = { PictoTalkTopAppBar(SettingsManager(LocalContext.current)) },
                    onDeckClicked = {
                        navController.navigate(PictoTalkScreen.Game.name)
                    }
                )
            }
            composable(PictoTalkScreen.Game.name) {
                GameScreen()
            }
        }
    }
}


