package com.example.pictotalk.data_access

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.pictotalk.data_access.BdOpenHelper.Companion.TABLE_CARD
import com.example.pictotalk.entities.Pictogram
import com.example.pictotalk.game.Difficulty

/**
 * Data Access Object for the Pictogram entity
 * @property sqliteDatabase
 * @constructor
 * Creates a new instance of the class
 * @property context
 */
class PictogramDAO (context: Context){
    var sqliteDatabase: SQLiteDatabase = BdOpenHelper.getInstance(context).writableDatabase

    // Function that retrieves a card from a cursor
    private fun getCardFromCursor(cursor: Cursor): Pictogram? {
        val idIndex = cursor.getColumnIndex("id")
        val phraseIndex = cursor.getColumnIndex("phrase")
        val imageIndex = cursor.getColumnIndex("image")
        val difficultyIndex = cursor.getColumnIndex("difficulty")

        return if (idIndex != -1 && phraseIndex != -1 && imageIndex != -1 && difficultyIndex != -1) {
            val id = cursor.getInt(idIndex)
            val phrase = cursor.getString(phraseIndex)
            val image = cursor.getInt(imageIndex)
            val difficulty = Difficulty.valueOf(cursor.getString(difficultyIndex))
            Pictogram(id, phrase, image, difficulty)
        } else {
            null
        }
    }

    // Function that inserts a pictogram into the database
    fun insertCard(pictogram: Pictogram): Long {
        val values = ContentValues().apply {
            put("phrase", pictogram.phrase)
            put("image", pictogram.image)
        }
        return sqliteDatabase.insert(TABLE_CARD, null, values)
    }

    // Function that retrieves all the cards from the database
    fun getAllPictograms(): List<Pictogram> {
        val pictograms = mutableListOf<Pictogram>()
        val cursor = sqliteDatabase.query(TABLE_CARD, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                getCardFromCursor(cursor)?.let { pictograms.add(it) }
            }
        }
        return pictograms
    }

    // Function that retrieves a card from the database by its id
    fun getCardById(id: Int): Pictogram? {
        val cursor = sqliteDatabase.query(TABLE_CARD, null, "id = ?", arrayOf(id.toString()), null, null, null)
        with(cursor) {
            if (moveToNext()) {
                return getCardFromCursor(cursor)
            }
        }
        return null
    }

    // Function that updates a pictogram in the database
    fun updateCard(pictogram: Pictogram): Int {
        val values = ContentValues().apply {
            put("phrase", pictogram.phrase)
            put("image", pictogram.image)
        }
        return sqliteDatabase.update(TABLE_CARD, values, "id = ?", arrayOf(pictogram.id.toString()))
    }

    // Function that deletes a pictogram from the database
    fun deleteCard(pictogram: Pictogram): Int {
        return sqliteDatabase.delete(TABLE_CARD, "id = ?", arrayOf(pictogram.id.toString()))
    }

    // Function that retrieves all the cards from the database by a deck
    fun getCardsByDeckId(deckId: Int): List<Pictogram> {
        val pictograms = mutableListOf<Pictogram>()
        val cursor = sqliteDatabase.rawQuery(
            "SELECT c.id, c.phrase, c.image, c.difficulty FROM card c " +
                    "JOIN card_deck cd ON c.id = cd.card_id " +
                    "WHERE cd.deck_id = ?",
            arrayOf(deckId.toString())
        )
        with(cursor) {
            while (moveToNext()) {
                getCardFromCursor(cursor)?.let { pictograms.add(it) }
            }
        }
        return pictograms
    }
}