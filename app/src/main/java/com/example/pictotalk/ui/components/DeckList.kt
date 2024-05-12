package com.example.pictotalk.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pictotalk.StateManager
import com.example.pictotalk.entities.Deck

@Composable
fun DeckList(decks: List<Deck>, paddingValues: PaddingValues, onDeckClicked: () -> Unit = {}) {
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
                    DeckCard(deck, onDeckClicked = {
                        onDeckClicked()
                        val vm = StateManager.getInstance()
                        vm.deck = deck
                    })
                }
            }
        }
    }
}

