package com.example.flashcardapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object DeckList

@Serializable
data class SelectedDeckPage(
    val deckId: Int
)

@Serializable
data class AddNewDeck(
    val newDeckId: Int
)

@Serializable
data class AddNewCard(
    val deckId: Int,
    val deckName: String
)

@Serializable
object CardQuiz

@Serializable
data class AllCards(
    val deckId: Int,
    val deckName: String
)

@Serializable
object UserProfile