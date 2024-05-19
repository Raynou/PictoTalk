package com.example.pictotalk.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictotalk.StateManager
import com.example.pictotalk.ui.components.AddFAB
import com.example.pictotalk.ui.components.DeckList
import com.example.pictotalk.data_access.DeckDAO
import com.example.pictotalk.data_access.PictogramDAO
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.ui.components.ConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    topAppBar: @Composable () -> Unit = {},
    onDeckClicked: () -> Unit = {},
    onLongDeckClicked: () -> Unit = {},
    onFABClicked: () -> Unit = {}
) {
    val deckDAO = DeckDAO(LocalContext.current)
    val pictogramDAO = PictogramDAO(LocalContext.current)
    val decks = deckDAO.getAllDecks().map { it }
    val showBottomSheet = remember { mutableStateOf(false) }
    val stateManager = StateManager.getInstance()

    Scaffold(
        floatingActionButton = {
            AddFAB(
                onClick = onFABClicked
            )
        },
        topBar = { topAppBar() }
    ) { innerPadding ->
        DeckList(
            decks = decks,
            paddingValues = innerPadding,
            onDeckClicked = onDeckClicked,
            onLongDeckClicked = { 
                showBottomSheet.value = true
            }
        )
    }

    if(showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false }
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stateManager.deck.name,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row (
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    // Add button for delete and edit
                    TextButton(
                        onClick = {
                            deckDAO.deleteDeck(stateManager.deck.id!!)
                            showBottomSheet.value = false
                            stateManager.deck = Deck()
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete deck")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Eliminar")
                    }

                    TextButton(
                        onClick = {
                            val deckPictograms = pictogramDAO.getCardsByDeckId(stateManager.deck.id!!)
                            showBottomSheet.value = false
                            /**
                             * The reason because the name of these properties is newDeckPictograms & newDeckName
                             * is because the NewDeckScreen is going to use this property to show the pictograms of the deck
                             * that's is going to be created. However, to avoid repeating the same code to show the
                             * pictograms of the deck that's going to be edited, we are going to use the same property.
                             */
                            stateManager.newDeckName = stateManager.deck.name
                            stateManager.newDeckPictograms = deckPictograms.toMutableList()
                            onLongDeckClicked()
                        }
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit deck")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Editar")
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainMenuDefaultPreview() {
    MainMenuScreen()
}