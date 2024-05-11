package com.example.pictotalk

import com.example.pictotalk.entities.Card
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
        val gameManager = GameManager(easyGame)

        val card = Card(1, "Hola", 1)
        val answer = "Ho"

        gameManager.evaluateAnswer(card, answer)

        assertEquals(1, gameManager.score)
    }

    @Test
    fun testEvaluateAnswerInEasyGameAndIncorrectAnswer() {
        val gameManager = GameManager(easyGame)

        val card = Card(1, "Hola", 1)
        val answer = "ob"

        gameManager.evaluateAnswer(card, answer)

        assertEquals(0, gameManager.score)
    }

    @Test
    fun testEvaluateAnswerInMediumGameAndCorrectAnswer() {
        val gameManager = GameManager(mediumGame)

        val card = Card(1, "Hola mundo", 1)
        val answer = "oda mudo"

        gameManager.evaluateAnswer(card, answer)

        assertEquals(1, gameManager.score)
    }

    @Test
    fun testEvaluateAnswerInMediumGameAndIncorrectAnswer() {
        val gameManager = GameManager(mediumGame)

        val card = Card(1, "Hola mundo", 1)
        val answer = "oda"

        gameManager.evaluateAnswer(card, answer)

        assertEquals(0, gameManager.score)
    }


    @Test
    fun testEvaluateAnswerInHardGameAndCorrectAnswer() {
        val gameManager = GameManager(hardGame)

        val card = Card(1, "Hola", 1)
        val answer = "hola"

        gameManager.evaluateAnswer(card, answer)

        assertEquals(1, gameManager.score)
    }
    @Test
    fun testEvaluateAnswerInHardGameAndIncorrectAnswer() {
        val gameManager = GameManager(hardGame)

        val card = Card(1, "Hola", 1)
        val answer = "ola"

        gameManager.evaluateAnswer(card, answer)

        assertEquals(0, gameManager.score)
    }
}