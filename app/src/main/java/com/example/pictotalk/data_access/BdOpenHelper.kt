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
                    "image TEXT," +
                    "difficulty TEXT)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_CARD_DECK (" +
                    "card_id INTEGER," +
                    "deck_id INTEGER," +
                    "FOREIGN KEY(card_id) REFERENCES $TABLE_CARD(id)," +
                    "FOREIGN KEY(deck_id) REFERENCES $TABLE_DECK(id)," +
                    "PRIMARY KEY(card_id, deck_id))"
        )
        val image = R.drawable.cards
        db.execSQL("INSERT INTO $TABLE_DECK (name, description, image) VALUES ('Cotidianidad', 'Mazo con pictogramas relacionados con el día a día', $image)")
        // Insertar 30 cartas en la tabla card
        val cards = listOf(
            Triple("Pedir", R.drawable.question, "EASY"),
            Triple("Vaso de agua", R.drawable.water, "HARD"),
            Triple("Ojos", R.drawable.eyes, "EASY"),
            Triple("Orejas", R.drawable.ear, "MEDIUM"),
            Triple("Boca", R.drawable.sabroso, "EASY"),
            Triple("Cachetes", R.drawable.guy, "MEDIUM"),
            Triple("Brazos", R.drawable.arm, "EASY"),
            Triple("Piernas", R.drawable.leg, "EASY"),
            Triple("Baño", R.drawable.publictoilet, "EASY"),
            Triple("Balón", R.drawable.football, "EASY"),
            Triple("Apagar la luz", R.drawable.turnoff, "HARD"),
            Triple("Irse a dormir", R.drawable.sleep, "HARD"),
            Triple("Levantarse", R.drawable.getup, "MEDIUM"),
            Triple("Jugar", R.drawable.playing, "EASY"),
            Triple("Cocinar", R.drawable.cooking, "MEDIUM"),
            Triple("Estudiar", R.drawable.studying, "MEDIUM"),
            Triple("Encender la luz", R.drawable.lightbulb, "HARD"),
            Triple("Ir a la escuela", R.drawable.school, "HARD"),
            Triple("Sentarse", R.drawable.sitting, "EASY"),
            Triple("Lavarse los dientes", R.drawable.brush, "HARD"),
            Triple("Bañarse", R.drawable.shower, "EASY"),
            Triple("Secarse", R.drawable.wipe, "EASY"),
            Triple("Sonarse la nariz", R.drawable.sneeze, "HARD"),
            Triple("Usar la computadora", R.drawable.computer, "HARD"),
            Triple("Usar el celular", R.drawable.cellphone, "HARD"),
            Triple("Comer", R.drawable.eat, "EASY"),
            Triple("Correr", R.drawable.run, "EASY"),
            Triple("Saltar", R.drawable.jump, "EASY"),
            Triple("Leer", R.drawable.read, "EASY"),
            Triple("Escribir", R.drawable.write, "MEDIUM")
        )
        cards.forEach {
            db.execSQL("INSERT INTO $TABLE_CARD (phrase, image, difficulty) VALUES ('${it.first}', ${it.second}, '${it.third}')")
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
