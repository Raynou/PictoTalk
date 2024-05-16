package com.example.pictotalk.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pictotalk.ui.components.AddFAB
import com.example.pictotalk.ui.components.DeckList
import com.example.pictotalk.data_access.DeckDAO

@Composable
fun MainMenuScreen(
    topAppBar: @Composable () -> Unit = {},
    onDeckClicked: () -> Unit = {},
    onFABClicked: () -> Unit = {}
) {
    val deckDAO = DeckDAO(LocalContext.current)
    val decks = deckDAO.getAllDecks().map { it }

    Scaffold(
        floatingActionButton = {
            AddFAB(
                onClick = onFABClicked
            )
        },
        topBar = { topAppBar() }
    ) { innerPadding ->
        DeckList(decks, innerPadding, onDeckClicked)
    }
}
@Preview(showBackground = true)
@Composable
fun MainMenuDefaultPreview() {
    MainMenuScreen()
}