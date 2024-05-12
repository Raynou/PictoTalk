package com.example.pictotalk.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictotalk.entities.Deck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckCard(deck: Deck, onDeckClicked: () -> Unit = {}) {
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
            onDeckClicked()
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