package com.example.flashcardapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true) val deckId: Int = 0,
    val name: String,
    val description: String,
    val numOfCards: Int
)

object DeckConstant {
    const val DECKNAMELENGTH = 25
    const val DECKDESCRIPTIONLENGTH = 100
}