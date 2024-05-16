package com.example.pictotalk.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pictotalk.R
import com.example.pictotalk.StateManager
import com.example.pictotalk.data_access.DeckDAO
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.game.Difficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDeckScreen(
    topAppBar: @Composable () -> Unit = {},
    onDeckClicked: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    val textFieldState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val stateManager = StateManager.getInstance()
    val deckDAO = DeckDAO(context)
    Log.d("NewDeckScreen", "StateManager.newDeckPictograms: ${stateManager.newDeckPictograms}")
    Scaffold(
        topBar = { topAppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(27.dp))

            OutlinedTextField(
                value = textFieldState.value,
                singleLine = true,
                placeholder = { Text("Nombre del mazo") },
                trailingIcon = {
                    IconButton(onClick = {
                        textFieldState.value = ""
                    }) {
                        Icon(
                            Icons.Outlined.Cancel,
                            contentDescription = "Check",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                onValueChange = {
                    newValue ->
                    textFieldState.value = if (newValue.length <= 15) newValue else newValue.take(15)
                },
                modifier = Modifier
                    .width(313.dp)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            Card (
                modifier = Modifier
                    .width(313.dp)
                    .height(435.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = {
                    onDeckClicked()
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.cards),
                    contentDescription = "YO"
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
            // Big floating action button with check icon
            FloatingActionButton(
                onClick = {
                    if(textFieldState.value.isEmpty()){
                        Toast.makeText(
                            context,
                            "El nombre del mazo no puede estar vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    if (stateManager.newDeckPictograms.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Agrega al menos un pictograma al mazo",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    if(stateManager.newDeckPictograms.any { it.difficulty != Difficulty.EASY }) {
                        Toast.makeText(
                            context,
                            "El mazo debe contener al menos un pictograma fácil",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    if(stateManager.newDeckPictograms.any { it.difficulty != Difficulty.MEDIUM }) {
                        Toast.makeText(
                            context,
                            "El mazo debe contener al menos un pictograma medio",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    if(stateManager.newDeckPictograms.any { it.difficulty != Difficulty.HARD }) {
                        Toast.makeText(
                            context,
                            "El mazo debe contener al menos un pictograma difícil",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    val newDeck = Deck(
                        name = textFieldState.value,
                        image = R.drawable.cards
                    )
                    // Crate new Deck
                    val newDeckID = deckDAO.insertDeck(newDeck).toInt()
                    if(newDeckID == -1){
                        Toast.makeText(
                            context,
                            "Error al crear el mazo",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    // Reset the cards associated with the new deck
                    deckDAO.resetAssociatedCards(newDeckID)
                    // Associate cards with the new deck
                    deckDAO.associateCards(newDeckID, stateManager.newDeckPictograms!!)
                },
                modifier = Modifier
                    .size(96.dp)
            ) {
                 Icon(
                     Icons.Filled.Check,
                     contentDescription = "Check",
                     modifier = Modifier
                         .size(24.dp)
                 )
            }
        }
    }
    // on back pressed
    BackHandler {
        // Clear newDeckPictograms
        stateManager.newDeckPictograms = mutableListOf()
        // Go back to main menu
        navigateUp()
    }
    // Destroy stateManager.newDeckPictograms on dismiss
    //LaunchedEffect(key1 = true) {
    //    stateManager.newDeckPictograms = mutableListOf()
    //}
}
@Preview(showBackground = true)
@Composable
fun NewDeckDefaultPreview() {
    NewDeckScreen()
}