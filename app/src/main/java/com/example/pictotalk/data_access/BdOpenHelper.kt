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
                    "image TEXT," +
                    "difficulty TEXT)"
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
        // Insertar 30 cartas en la tabla card
        val cards = listOf(
            Triple("Pedir", R.drawable.cards, "EASY"),
            Triple("Vaso de agua", R.drawable.cards, "EASY"),
            Triple("Ojos", R.drawable.cards, "MEDIUM"),
            Triple("Orejas", R.drawable.cards, "MEDIUM"),
            Triple("Boca", R.drawable.cards, "HARD"),
            Triple("Cachetes", R.drawable.cards, "HARD"),
            Triple("Brazos", R.drawable.cards, "EASY"),
            Triple("Piernas", R.drawable.cards, "EASY"),
            Triple("Baño", R.drawable.cards, "MEDIUM"),
            Triple("Balón", R.drawable.cards, "MEDIUM"),
            Triple("Piernas", R.drawable.cards, "HARD"),
            Triple("Dormirse", R.drawable.cards, "HARD"),
            Triple("Levantarse", R.drawable.cards, "EASY"),
            Triple("Jugar", R.drawable.cards, "EASY"),
            Triple("Cocinar", R.drawable.cards, "MEDIUM"),
            Triple("Estudiar", R.drawable.cards, "MEDIUM"),
            Triple("Encender la luz", R.drawable.cards, "HARD"),
            Triple("Ir a la escuela", R.drawable.cards, "HARD"),
            Triple("Sentarse", R.drawable.cards, "EASY"),
            Triple("Lavarse los dientes", R.drawable.cards, "EASY"),
            Triple("Bañarse", R.drawable.cards, "MEDIUM"),
            Triple("Secarse", R.drawable.cards, "MEDIUM"),
            Triple("Sonarse la nariz", R.drawable.cards, "HARD"),
            Triple("Usar la computadora", R.drawable.cards, "HARD"),
            Triple("Usar el celular", R.drawable.cards, "EASY"),
            Triple("Comer", R.drawable.cards, "EASY"),
            Triple("Correr", R.drawable.cards, "MEDIUM"),
            Triple("Saltar", R.drawable.cards, "HARD"),
            Triple("Leer", R.drawable.cards, "EASY"),
            Triple("Escribir", R.drawable.cards, "MEDIUM")
        )
        cards.forEach {
            db.execSQL("INSERT INTO $TABLE_CARD (phrase, image) VALUES ('${it.first}', ${it.second}, ${it.third})")
        }

        // Asociar todas las cartas al mazo con id 1
        val cardsIds = (1..30).toList()
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
