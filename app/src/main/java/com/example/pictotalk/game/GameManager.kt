package com.example.pictotalk.game

import com.example.pictotalk.entities.Pictogram

/***
 * Class that manages the game logic
 * @property settingsManager
 * @property difficulty
 * @property score
 * @property cards
 * @constructor
 * @param context
 */
class GameManager (var difficulty: Difficulty, pictograms: List<Pictogram>, var score: Int = 0) {

    private val activePictograms: List<Pictogram> = pictograms.filter { it.difficulty == difficulty }.shuffled()
    private var currentCardIndex = 0

    fun getTotalcards(): Int {
        return activePictograms.size
    }

    // Get the current card and increase the index
    fun getCurrentCard(): Pictogram {
        val card = activePictograms[currentCardIndex]
        currentCardIndex++
        return card
    }

    fun evaluateAnswer(answer: String) {
        val card = activePictograms[currentCardIndex]
        if(isCorrectAnswer(card, answer)) {
            // Increase the score
            score++
        }
    }

    // Function to check if the answer is correct
    private fun isCorrectAnswer(pictogram: Pictogram, answer: String): Boolean {
        return when(difficulty) {
            Difficulty.EASY -> {
                // The answer should be almost 30% like the phrase
                comparePhrases(pictogram.phrase, answer, 0.30f)
            }
            Difficulty.MEDIUM -> {
                // The answer should be almost 40% like the phrase
                comparePhrases(pictogram.phrase, answer, 0.40f)
            }
            else -> {
                pictogram.phrase.lowercase() == answer.lowercase()
            }
        }
    }

    // Evaluate the given answer versus the card's phrase with a given similarity
    private fun comparePhrases(phrase: String, answer: String, similarity: Float) : Boolean {
        val distance = levenshteinDistance(phrase.lowercase(), answer.lowercase())
        val maxLength = maxOf(phrase.length, answer.length)
        return (maxLength - distance).toFloat() / maxLength >= similarity
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length

        // Crear una matriz de tamaño (m + 1) x (n + 1)
        val dp = Array(m + 1) { IntArray(n + 1) }

        // Inicializar la primera fila y columna
        for (i in 0..m) {
            dp[i][0] = i
        }
        for (j in 0..n) {
            dp[0][j] = j
        }

        // Rellenar la matriz usando las operaciones de edición
        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1

                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,    // Eliminación
                    dp[i][j - 1] + 1,    // Inserción
                    dp[i - 1][j - 1] + cost  // Sustitución
                )
            }
        }
        return dp[m][n]
    }
}