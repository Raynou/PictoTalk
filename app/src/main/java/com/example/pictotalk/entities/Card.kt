package com.example.pictotalk.entities

import com.example.pictotalk.game.Difficulty

/**
 * Entity that represents a card
 * @property id
 * @property phrase
 * @property image
 */
class Card(
    var id: Int,
    var phrase: String,
    var image: Int,
    var difficulty: Difficulty
    )