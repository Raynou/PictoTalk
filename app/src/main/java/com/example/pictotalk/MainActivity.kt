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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

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
    Scaffold(
        topBar = { PictoTalkTopAppBar() },
        floatingActionButton = { AddDeckFAB() }
    ) { innerPadding ->
        DeckList(innerPadding)
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictoTalkTopAppBar() {
    TopAppBar(
        title = { Text("PictoTalk") },
        actions = {
            IconButton(onClick = { /* handle actions */ }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Yes")
            }
        }
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictoTalkTopAppBar() {
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("PictoTalk") },
        actions = {
            // Icono para desplegar el menú
            IconButton(onClick = { expanded.value = true }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Más opciones")
            }
            // Menú desplegable
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        // Aquí manejas el cambio de dificultad
                        expanded.value = false
                    },
                    text = {
                        Text("Cambiar dificultad")
                    }
                )
            }
        }
    )
}


@Composable
fun AddDeckFAB() {
    FloatingActionButton(onClick = { /* handle FAB click */ }) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }
}

@Composable
fun DeckList(paddingValues: PaddingValues) {
    val decks = listOf("Mazo 1", "Mazo 2", "Mazo 3", "Mazo 4", "Mazo 5") // Mock data

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

@Composable
fun DeckCard(deckName: String) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp),
        // shape = CutCornerShape(20.dp)
        elevation = CardDefaults.cardElevation(10.dp),
        //border = BorderStroke(3.dp,Color.Gray)
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use a placeholder for the image
            Image(
                painter = painterResource(id = R.drawable.cards),
                contentDescription = deckName
            )
            Text(deckName, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PictoTalkScreen()
}


