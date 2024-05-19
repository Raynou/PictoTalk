package com.example.pictotalk.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pictotalk.StateManager
import com.example.pictotalk.entities.Deck

@Composable
fun DeckList(
    decks: List<Deck>,
    paddingValues: PaddingValues,
    onLongDeckClicked: () -> Unit = {},
    onDeckClicked: () -> Unit = {}
) {
    val stateManager = StateManager.getInstance()
    LazyColumn(
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(decks.chunked(2)) { rowDecks ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
            ) {
                for (deck in rowDecks) {
                    DeckCard(
                        deck,
                        onDeckClicked = {
                            onDeckClicked()
                            stateManager.deck = deck
                        },
                        onLongDeckClicked = {
                            onLongDeckClicked()
                            stateManager.deck = deck
                        }
                    )
                }
                if (rowDecks.size < 2) {
                    Spacer(modifier = Modifier.width(180.dp).height(210.dp))
                }
            }
        }
    }
}

