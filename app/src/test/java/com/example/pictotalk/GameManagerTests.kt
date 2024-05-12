package com.example.pictotalk

import com.example.pictotalk.entities.Pictogram
import com.example.pictotalk.game.Difficulty
import com.example.pictotalk.game.GameManager
import org.junit.Assert.*
import org.junit.Test

class GameManagerTest {

    val easyGame: Difficulty = Difficulty.EASY
    val mediumGame: Difficulty = Difficulty.MEDIUM
    val hardGame: Difficulty = Difficulty.HARD

    @Test
    fun testEvaluateAnswerInEasyGameAndCorrectAnswer() {
        val pictograms = listOf(Pictogram(1, "Hola", 1, easyGame))
        val gameManager = GameManager(easyGame, pictograms)

        val answer = "Ho"

        gameManager.evaluateAnswer(answer)

        assertEquals(1, gameManager.score)
    }

    @Test fun testEvaluateAnswerInEasyGameAndIncorrectAnswer() {
        val pictograms = listOf(Pictogram(1, "Hola", 1, easyGame))
        val gameManager = GameManager(easyGame, pictograms)

        val answer = "ob"

        gameManager.evaluateAnswer(answer)

        assertEquals(0, gameManager.score)
    }

    @Test
    fun testEvaluateAnswerInMediumGameAndCorrectAnswer() {
        val pictograms = listOf(Pictogram(1, "Hola mundo", 1, mediumGame))
        val gameManager = GameManager(mediumGame, pictograms)

        val answer = "oda mudo"

        gameManager.evaluateAnswer(answer)

        assertEquals(1, gameManager.score)
    }

    @Test
    fun testEvaluateAnswerInMediumGameAndIncorrectAnswer() {
        val pictograms = listOf(Pictogram(1, "Hola mundo", 1, mediumGame))
        val gameManager = GameManager(mediumGame, pictograms)

        val answer = "oda"

        gameManager.evaluateAnswer(answer)

        assertEquals(0, gameManager.score)
    }


    @Test
    fun testEvaluateAnswerInHardGameAndCorrectAnswer() {
        val pictograms = listOf(Pictogram(1, "Hola", 1, hardGame))
        val gameManager = GameManager(hardGame, pictograms)

        val answer = "hola"

        gameManager.evaluateAnswer(answer)

        assertEquals(1, gameManager.score)
    }
    @Test
    fun testEvaluateAnswerInHardGameAndIncorrectAnswer() {
        val pictograms = listOf(Pictogram(1, "Hola", 1, hardGame))
        val gameManager = GameManager(hardGame, pictograms)

        val answer = "ola"

        gameManager.evaluateAnswer(answer)

        assertEquals(0, gameManager.score)
    }
}