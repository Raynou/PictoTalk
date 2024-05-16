package com.example.pictotalk.entities

/**
 * Entity class that represents a deck
 * @property id
 * @property name
 * @property description
 * @property image
 */
data class Deck(
    var id: Int? = null,
    var name: String,
    var description: String? = null,
    var image: Int,
) {
    // void constructor
    constructor() : this(0, "", "", 0)
}