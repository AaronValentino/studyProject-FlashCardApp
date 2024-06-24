package com.example.flashcardapp.deck

import androidx.lifecycle.ViewModel
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckNCardRepository

class AddNewDeckViewModel(
    private val deckNCardRepository: DeckNCardRepository
) : ViewModel() {

    suspend fun createNewDeck(deck: Deck) {
        deckNCardRepository.insertDeck(deck)
    }
}