package com.example.pictotalk.data_access

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.pictotalk.data_access.BdOpenHelper.Companion.TABLE_DECK
import com.example.pictotalk.entities.Deck

/**
 * Data Access Object for the Deck entity
 * @property sqliteDatabase
 * @constructor
 * Creates a new instance of the class
 * @property context
 */
class DeckDAO(context: Context) {
    var sqliteDatabase: SQLiteDatabase = BdOpenHelper.getInstance(context).writableDatabase

    // Function that retrieves a deck from a cursor
    private fun getDeckFromCursor(cursor: Cursor): Deck? {
        val idIndex = cursor.getColumnIndex("id")
        val nameIndex = cursor.getColumnIndex("name")
        val descriptionIndex = cursor.getColumnIndex("description")
        val imageIndex = cursor.getColumnIndex("image")

        return if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 && imageIndex != -1) {
            val id = cursor.getInt(idIndex)
            val name = cursor.getString(nameIndex)
            val description = cursor.getString(descriptionIndex)
            val image = cursor.getString(imageIndex)
            Deck(id, name, description, image, mutableListOf())
        } else {
            null
        }
    }

    // Function that inserts a deck into the database
    fun insertDeck(deck: Deck): Long {
        val values = ContentValues().apply {
            put("name", deck.name)
            put("description", deck.description)
            put("image", deck.image)
        }
        return sqliteDatabase.insert(TABLE_DECK, null, values)
    }

    // Function that retrieves all the decks from the database
    fun getAllDecks(): List<Deck> {
        val decks = mutableListOf<Deck>()
        val cursor = sqliteDatabase.query(TABLE_DECK, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                getDeckFromCursor(cursor)?.let { decks.add(it) }
            }
        }
        return decks
    }

    // Function that retrieves a deck from the database by its id
    fun getDeckById(id: Int): Deck? {
        val cursor = sqliteDatabase.query(
            TABLE_DECK,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            return if (moveToFirst()) {
                getDeckFromCursor(cursor)
            } else {
                null
            }
        }
    }

    // Function that retrieves all the decks by a card id
    fun getDecksByCardId(cardId: Int): List<Deck> {
        val decks = mutableListOf<Deck>()
        val query = "SELECT * FROM $TABLE_DECK WHERE id IN (SELECT deck_id FROM card_deck WHERE card_id = ?)"
        val cursor = sqliteDatabase.rawQuery(query, arrayOf(cardId.toString()))
        with(cursor) {
            while (moveToNext()) {
                getDeckFromCursor(cursor)?.let { decks.add(it) }
            }
        }
        return decks
    }
}