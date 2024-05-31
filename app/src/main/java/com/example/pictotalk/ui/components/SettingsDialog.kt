package com.example.pictotalk.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pictotalk.game.Difficulty
import com.example.pictotalk.game.SettingsManager
import com.example.pictotalk.ui.theme.CristalLake
import com.example.pictotalk.ui.theme.Magical
import com.example.pictotalk.ui.theme.MistyAqua
import com.example.pictotalk.ui.theme.SimplyElegant

@Composable
fun SettingsDialog(
    showDialog: MutableState<Boolean>,
    settingsManager: SettingsManager,
    radioGroupState: MutableState<Difficulty>,
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        //containerColor = Magical,
        title = { Text("Dificultad") },
        text = {
            Column {
                Text("Fácil: para niños de 2 a 5 años")
                Text("Medio: para niños de 5 a 7 años")
                Text("Difícil: 8 años en adelante")

                // Espacio en blanco
                Text("\n")

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    // remove on click effect
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    // Make all row clickable
                ){
                    Text("Fácil")
                    RadioButton(
                        selected = radioGroupState.value == Difficulty.EASY,
                        onClick = { radioGroupState.value = Difficulty.EASY },
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