package com.example.flashcardapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object DeckList

@Serializable
data class SelectedDeckPage(
    val deckId: Int,
    val cardsAdded: Int = 0
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
object CardList

@Serializable
object UserProfile