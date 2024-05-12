package com.example.pictotalk.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.pictotalk.StateManager
import com.example.pictotalk.data_access.PictogramDAO
import com.example.pictotalk.entities.Pictogram
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.game.GameManager
import com.example.pictotalk.game.SettingsManager

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.pictotalk.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun GameScreen() {
    val context = LocalContext.current
    val stateManager = StateManager.getInstance()
    val deck = stateManager.deck
    val cards = getCards(deck, context)
    val settingsManager = SettingsManager(context)
    val difficulty = settingsManager.getDifficulty()
    val gameManager = GameManager(difficulty, cards)

    // Status variables
    val progressStatus by gameManager.getProgress()
    val currentCard by gameManager.getCurrentCard()

    Scaffold { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }


                // Simulate game progress
                LinearProgressIndicator(
                    progress = progressStatus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Text(currentCard.phrase, fontSize = 24.sp)

            Column (
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(400.dp)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(painter = painterResource(id = R.drawable.cards), contentDescription = "Pictogram Image") // replace with actual image
                    }
                }

            }

            FloatingActionButton(
                onClick = {
                    gameManager.nextCard()
                },
                modifier = Modifier
                            .size(72.dp)
            ) {
                Icon(Icons.Filled.Build, contentDescription = "Speak")
            }
        }
    }
}

fun getCards(deck: Deck, context: Context): List<Pictogram> {
    val cardDAO = PictogramDAO(context)
    return cardDAO.getCardsByDeckId(deck.id)
}

@Preview(showBackground = true)
@Composable
fun GameDefaultPreview() {
    GameScreen()
}

