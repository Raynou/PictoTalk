package com.example.pictotalk.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictotalk.R
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.ui.theme.CristalLake
import com.example.pictotalk.ui.theme.Magical
import com.example.pictotalk.ui.theme.MistyAqua

@OptIn( ExperimentalFoundationApi::class)
@Composable
fun DeckCard(
    deck: Deck,
    onDeckClicked: () -> Unit = {},
    onLongDeckClicked: () -> Unit = {}
) {
    val backgroundColor = Color.White
    //val backgroundColor = MistyAqua
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(230.dp)
            .combinedClickable(
                onClick = { onDeckClicked() },
                onLongClick = { onLongDeckClicked() }
            ),
        //elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        // Add stroke to the card
        border = BorderStroke(3.dp, Color.Black),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use a placeholder for the image
            Box (
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(0.dp)),
                contentAlignment = androidx.compose.ui.Alignment.Center,
            ){
                Image(
                    painter = painterResource(id = R.drawable.add),
                    modifier = Modifier.padding(16.dp),
                    contentDescription = deck.name
                )
            }
            Divider(color = Color.Black, thickness = 3.dp)
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                // center text in the box vertically
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(deck.name, fontSize = 16.sp, color = Color.Black)
            }
        }
    }
}