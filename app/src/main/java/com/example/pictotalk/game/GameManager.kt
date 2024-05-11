package com.example.pictotalk.game

import android.content.Context
import com.example.pictotalk.entities.Card

/***
 * Class that manages the game logic
 * @property settingsManager
 * @property difficulty
 * @constructor
 * @param context
 */
class GameManager (var difficulty: Difficulty, var score: Int = 0, private var totalCards: Int = 0) {

    fun getTotalcards(): Int {
        return totalCards
    }

    fun evaluateAnswer(card: Card, answer: String) {
        if(isCorrectAnswer(card, answer)) {
            // Increase the score
            score++
        }
    }

    // Function to check if the answer is correct
    private fun isCorrectAnswer(card: Card, answer: String): Boolean {
        return when(difficulty) {
            Difficulty.EASY -> {
                // The answer should be almost 25% like the phrase
                comparePhrases(card.phrase, answer, 0.25f)
            }
            Difficulty.MEDIUM -> {
                // The answer should be almost 50% like the phrase
                comparePhrases(card.phrase, answer, 0.35f)
            }
            else -> {
                card.phrase == answer
            }
        }
    }

    // Evaluate the given answer versus the card's phrase with a given similarity
    private fun comparePhrases(phrase: String, answer: String, similarity: Float) : Boolean {
        val phraseLength = phrase.length
        val answerLength = answer.length
        val maxLength = if(phraseLength > answerLength) phraseLength else answerLength
        val minLength = if(phraseLength < answerLength) phraseLength else answerLength
        var correctChars = 0
        for(i in 0 until minLength) {
            if(phrase[i].lowercaseChar() == answer[i].lowercaseChar()) {
                correctChars++
            }
        }
        return correctChars >= maxLength * similarity
    }
}