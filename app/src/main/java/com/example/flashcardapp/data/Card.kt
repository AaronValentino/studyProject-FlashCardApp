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

//fun createCardsData(maxCard: Int): List<Card> {
//    val maxRandNum = (1..maxCard).random()
//    return (1..maxRandNum).map {
//        Card("Question $it", "Answer $it")
//    }
//}