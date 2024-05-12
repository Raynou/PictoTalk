package com.example.pictotalk.entities

/**
 * Entity class that represents a deck
 * @property id
 * @property name
 * @property description
 * @property image
 */
class Deck(
    var id: Int,
    var name: String,
    var description: String,
    var image: Int,
) {
    // void constructor
    constructor() : this(0, "", "", 0)
}