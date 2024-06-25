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
object CardQuiz

@Serializable
object CardList

@Serializable
object UserProfile