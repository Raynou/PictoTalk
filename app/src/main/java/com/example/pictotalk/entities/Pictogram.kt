package com.example.pictotalk.entities

import com.example.pictotalk.game.Difficulty

/**
 * Entity that represents a pictogram
 * @property id
 * @property phrase
 * @property image
 */
data class Pictogram(
    var id: Int,
    var phrase: String,
    var image: Int,
    var difficulty: Difficulty
    )