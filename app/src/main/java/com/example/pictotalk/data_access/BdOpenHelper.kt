package com.example.pictotalk.data_access

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pictotalk.R

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
        val image = R.drawable.cards
        db.execSQL("INSERT INTO $TABLE_DECK (name, description, image) VALUES ('Cotidianidad', 'Mazo con pictogramas relacionados con el día a día', $image)")
        // Insertar 25 cartas en la tabla card
        val cards = listOf(
            Pair("Pedir", R.drawable.cards),
            Pair("Vaso de agua", R.drawable.cards),
            Pair("Ojos", R.drawable.cards),
            Pair("Orejas", R.drawable.cards),
            Pair("Boca", R.drawable.cards),
            Pair("Cachetes", R.drawable.cards),
            Pair("Brazos", R.drawable.cards),
            Pair("Piernas", R.drawable.cards),
            Pair("Baño", R.drawable.cards),
            Pair("Balón", R.drawable.cards),
            Pair("Piernas", R.drawable.cards),
            Pair("Dormirse", R.drawable.cards),
            Pair("Levantarse", R.drawable.cards),
            Pair("Jugar", R.drawable.cards),
            Pair("Cocinar", R.drawable.cards),
            Pair("Estudiar", R.drawable.cards),
            Pair("Encender la luz", R.drawable.cards),
            Pair("Ir a la escuela", R.drawable.cards),
            Pair("Sentarse", R.drawable.cards),
            Pair("Lavarse los dientes", R.drawable.cards),
            Pair("Bañarse", R.drawable.cards),
            Pair("Secarse", R.drawable.cards),
            Pair("Sonarse la nariz", R.drawable.cards),
            Pair("Usar la computadora", R.drawable.cards),
            Pair("Usar el celular", R.drawable.cards)
        )
        cards.forEach {
            db.execSQL("INSERT INTO $TABLE_CARD (phrase, image) VALUES ('${it.first}', ${it.second})")
        }

        // Asociar 10 cartas al mazo con id 1
        val cardsIds = (1..10).toList()
        cardsIds.forEach {
            db.execSQL("INSERT INTO $TABLE_CARD_DECK (card_id, deck_id) VALUES ($it, 1)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CARD_DECK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CARD")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DECK")
        onCreate(db)
    }
}
