package com.example.pictotalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pictotalk.data_access.DeckDAO
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.game.Difficulty
import com.example.pictotalk.game.SettingsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PictoTalkScreen()
        }
    }
}

@Composable
fun PictoTalkScreen() {
    val settingsManager = SettingsManager(LocalContext.current)
    Scaffold(
        topBar = { PictoTalkTopAppBar(settingsManager) },
        floatingActionButton = { AddDeckFAB() }
    ) { innerPadding ->
        DeckList(innerPadding)
    }
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
        val radioGroupState = rememberSaveable {mutableStateOf(settingsManager.getDifficulty())}
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
fun AddDeckFAB() {
    FloatingActionButton(onClick = { /* handle FAB click */ }) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }
}

@Composable
fun DeckList(paddingValues: PaddingValues) {
    val deckDAO = DeckDAO(LocalContext.current)
    val decks = deckDAO.getAllDecks().map { it }

    LazyColumn(
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(decks.chunked(2)) { rowDecks ->
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                for (deck in rowDecks) {
                    DeckCard(deck)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckCard(deck: Deck) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp),
        // shape = CutCornerShape(20.dp)
        elevation = CardDefaults.cardElevation(10.dp),
        //border = BorderStroke(3.dp,Color.Gray)
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {
            println(deck.id)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use a placeholder for the image
            Image(
                painter = painterResource(id = deck.image),
                contentDescription = deck.name
            )
            Text(deck.name, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PictoTalkScreen()
}




