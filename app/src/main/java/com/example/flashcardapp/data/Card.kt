package com.example.flashcardapp.card

data class Card(
    val question: String,
    val answer: String
)

fun createCardsData(maxCard: Int): List<Card> {
    val maxRandNum = (1..maxCard).random()
    return (1..maxRandNum).map {
        Card("Question $it", "Answer $it")
    }
}