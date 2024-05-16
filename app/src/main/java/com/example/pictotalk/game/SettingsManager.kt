package com.example.pictotalk.game

import android.content.Context
import android.content.SharedPreferences

/**
 * Singleton class that stores values that are intended to be used in various parts of the app.
 * @property context
 */
class SettingsManager(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "pictotalk"
        private const val PREFS_DIFFICULTY = "difficulty"
    }

    private fun getPreference(key: String, defaultValue: String): String {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun setPreference(key: String, value: String) {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getDifficulty(): Difficulty {
        val difficulty = getPreference(PREFS_DIFFICULTY, Difficulty.EASY.name)
        return Difficulty.valueOf(difficulty)
    }

    fun setDifficulty(difficulty: Difficulty) {
        setPreference(PREFS_DIFFICULTY, difficulty.name)
    }
}