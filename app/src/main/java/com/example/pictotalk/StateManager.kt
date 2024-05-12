package com.example.pictotalk

import com.example.pictotalk.entities.Deck

class StateManager private constructor (
    var deck: Deck = Deck()
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