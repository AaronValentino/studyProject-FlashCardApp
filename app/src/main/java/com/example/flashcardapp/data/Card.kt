package com.example.flashcardapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = Deck::class,
            parentColumns = arrayOf("deckId"),
            childColumns = arrayOf("deckId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Card(
    @PrimaryKey(autoGenerate = true) val cardId: Int = 0,
    val deckId: Int,
    val question: String,
    val answer: String,
    val numOfTimePracticed: Int,
    val lastAnsweredCorrect: Boolean
)

object CardConstant {
    const val CARDQUESTIONLENGTH = 40
    const val CARDANSWERLENGTH = 40
}