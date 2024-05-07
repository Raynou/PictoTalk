package com.example.pictotalk.data_access

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Class that represents the structure of the database
 * @param context
 * @param name
 * @param factory
 * @param version
 * @constructor
 * Creates a new instance of the class
 * @property context
 * @property name
 * @property factory
 * @property version
 * @property TABLE_DECK
 * @property TABLE_CARD
 */
class BdOpenHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "pictotalk.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: BdOpenHelper? = null

        fun getInstance(context: Context): BdOpenHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BdOpenHelper(context).also { INSTANCE = it }
            }
        const val TABLE_DECK = "deck"
        const val TABLE_CARD = "card"
        const val TABLE_CARD_DECK = "card_deck"
    }
    /**
     * Function that creates the tables in the database
     * @param db
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_DECK (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "image TEXT)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_CARD (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "phrase TEXT," +
                    "image TEXT" +
                    ")"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_CARD_DECK (" +
                    "card_id INTEGER," +
                    "deck_id INTEGER," +
                    "FOREIGN KEY(card_id) REFERENCES $TABLE_CARD(id)," +
                    "FOREIGN KEY(deck_id) REFERENCES $TABLE_DECK(id))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CARD")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DECK")
        onCreate(db)
    }
}
