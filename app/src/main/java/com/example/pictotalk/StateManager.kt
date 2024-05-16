package com.example.pictotalk

import com.example.pictotalk.entities.Deck
import com.example.pictotalk.entities.Pictogram
import com.example.pictotalk.game.GameManager

class StateManager private constructor (
    var deck: Deck = Deck(),
    var gameManager: GameManager? = null,
    var newDeckPictograms: MutableList<Pictogram> = mutableListOf(),
    var newDeckName: String? = null
) {
    companion object {
        private var instance: StateManager? = null

        fun getInstance(): StateManager {
            if (instance == null) {
                instance = StateManager()
            }
            return instance as StateManager
        }
    }

}