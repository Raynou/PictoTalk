package com.example.pictotalk.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictotalk.entities.Deck

@OptIn( ExperimentalFoundationApi::class)
@Composable
fun DeckCard(
    deck: Deck,
    onDeckClicked: () -> Unit = {},
    onLongDeckClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(210.dp)
            .combinedClickable(
                onClick = { onDeckClicked() } ,
                onLongClick = { onLongDeckClicked() }
            ),
        elevation = CardDefaults.cardElevation(10.dp),
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
                painter = painterResource(id = deck.image),
                contentDescription = deck.name
            )
            Text(deck.name, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}