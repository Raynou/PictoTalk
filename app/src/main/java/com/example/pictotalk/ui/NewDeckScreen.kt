package com.example.pictotalk.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pictotalk.R
import com.example.pictotalk.StateManager
import com.example.pictotalk.data_access.DeckDAO
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.game.CrudAction
import com.example.pictotalk.game.Difficulty
import com.example.pictotalk.game.CrudAction.EDIT;

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDeckScreen(
    topAppBar: @Composable () -> Unit = {},
    onDeckClicked: () -> Unit = {},
    navigateUp: () -> Unit = {},
    crudAction: CrudAction = CrudAction.CREATE
) {
    val context = LocalContext.current
    val stateManager = StateManager.getInstance()
    val deckDAO = DeckDAO(context)
    val textFieldState = remember { mutableStateOf(stateManager.newDeckName ?: "") }

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
                    stateManager.newDeckName = textFieldState.value
                    onDeckClicked()
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.add),
                        contentDescription = "Pictograms"
                    )
                }

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
                    if(stateManager.newDeckPictograms.all { it.difficulty != Difficulty.EASY }) {
                        Toast.makeText(
                            context,
                            "El mazo debe contener al menos un pictograma fácil",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    if(stateManager.newDeckPictograms.all { it.difficulty != Difficulty.MEDIUM }) {
                        Toast.makeText(
                            context,
                            "El mazo debe contener al menos un pictograma medio",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }
                    if(stateManager.newDeckPictograms.all { it.difficulty != Difficulty.HARD }) {
                        Toast.makeText(
                            context,
                            "El mazo debe contener al menos un pictograma difícil",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }

                    // Edit an existent Deck
                    if(crudAction == EDIT) {
                        val deckID = stateManager.deck.id!!
                        val updatedDeck = Deck(
                            id = deckID,
                            name = textFieldState.value,
                            image = R.drawable.cards
                        )
                        deckDAO.updateDeck(updatedDeck)
                        // Reset the cards associated with the new deck
                        deckDAO.resetAssociatedCards(deckID)
                        // Associate cards with the new deck
                        deckDAO.associateCards(deckID, stateManager.newDeckPictograms)
                    }else  {
                        val newDeck = Deck(
                            name = textFieldState.value,
                            image = R.drawable.cards
                        )
                        val newDeckID = deckDAO.insertDeck(newDeck).toInt()
                        // Crate new Deck
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
                        deckDAO.associateCards(newDeckID, stateManager.newDeckPictograms)
                    }

                    // Clear newDeckPictograms
                    stateManager.newDeckPictograms = mutableListOf()
                    stateManager.newDeckName = ""
                    // Go back to main menu
                    navigateUp()
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
        stateManager.newDeckName = ""
        // Go back to main menu
        navigateUp()
    }
}
@Preview(showBackground = true)
@Composable
fun NewDeckDefaultPreview() {
    NewDeckScreen()
}