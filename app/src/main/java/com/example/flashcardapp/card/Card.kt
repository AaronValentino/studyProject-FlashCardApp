package com.example.flashcardapp.card

data class Card(
    val question: String,
    val answer: String
)

object CardsData {
    val deck = (1..100).map {
        Card("Question $it", "Answer $it")
    }
}