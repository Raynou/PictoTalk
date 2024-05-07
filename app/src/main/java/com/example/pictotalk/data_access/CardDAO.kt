package com.example.pictotalk.data_access

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.pictotalk.data_access.BdOpenHelper.Companion.TABLE_CARD
import com.example.pictotalk.entities.Card

/**
 * Data Access Object for the Card entity
 * @property sqliteDatabase
 * @constructor
 * Creates a new instance of the class
 * @property context
 */
class CardDAO (context: Context){
    var sqliteDatabase: SQLiteDatabase = BdOpenHelper.getInstance(context).writableDatabase

    // Function that retrieves a card from a cursor
    private fun getCardFromCursor(cursor: Cursor): Card? {
        val idIndex = cursor.getColumnIndex("id")
        val phraseIndex = cursor.getColumnIndex("phrase")
        val imageIndex = cursor.getColumnIndex("image")

        return if (idIndex != -1 && phraseIndex != -1 && imageIndex != -1) {
            val id = cursor.getInt(idIndex)
            val phrase = cursor.getString(phraseIndex)
            val image = cursor.getString(imageIndex)
            Card(id, phrase, image)
        } else {
            null
        }
    }

    // Function that inserts a card into the database
    fun insertCard(card: Card): Long {
        val values = ContentValues().apply {
            put("phrase", card.phrase)
            put("image", card.image)
        }
        return sqliteDatabase.insert(TABLE_CARD, null, values)
    }

    // Function that retrieves all the cards from the database
    fun getAllCards(): List<Card> {
        val cards = mutableListOf<Card>()
        val cursor = sqliteDatabase.query(TABLE_CARD, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                getCardFromCursor(cursor)?.let { cards.add(it) }
            }
        }
        return cards
    }

    // Function that retrieves a card from the database by its id
    fun getCardById(id: Int): Card? {
        val cursor = sqliteDatabase.query(TABLE_CARD, null, "id = ?", arrayOf(id.toString()), null, null, null)
        with(cursor) {
            if (moveToNext()) {
                return getCardFromCursor(cursor)
            }
        }
        return null
    }

    // Function that updates a card in the database
    fun updateCard(card: Card): Int {
        val values = ContentValues().apply {
            put("phrase", card.phrase)
            put("image", card.image)
        }
        return sqliteDatabase.update(TABLE_CARD, values, "id = ?", arrayOf(card.id.toString()))
    }

    // Function that deletes a card from the database
    fun deleteCard(card: Card): Int {
        return sqliteDatabase.delete(TABLE_CARD, "id = ?", arrayOf(card.id.toString()))
    }

    // Function that retrieves all the cards from the database by a deck
    fun getCardsByDeck(deckId: Int): List<Card> {
        val cards = mutableListOf<Card>()
        val cursor = sqliteDatabase.rawQuery(
            "SELECT c.id, c.phrase, c.image FROM card c " +
                    "JOIN card_deck cd ON c.id = cd.card_id " +
                    "WHERE cd.deck_id = ?",
            arrayOf(deckId.toString())
        )
        with(cursor) {
            while (moveToNext()) {
                getCardFromCursor(cursor)?.let { cards.add(it) }
            }
        }
        return cards
    }
}