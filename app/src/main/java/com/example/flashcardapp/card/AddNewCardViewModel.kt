package com.example.flashcardapp.card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.data.DeckNCardRepository

class AddNewCardViewModel(
    savedStateHandle: SavedStateHandle,
    private val deckNCardRepository: DeckNCardRepository
) : ViewModel() {
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])

    suspend fun createNewCard(question: String, answer: String) {
        deckNCardRepository.insertCard(
            Card(
                deckId = deckId,
                question = question,
                answer = answer,
                numOfTimePracticed = 0,
                lastAnsweredCorrect = false
            )
        )
    }
}