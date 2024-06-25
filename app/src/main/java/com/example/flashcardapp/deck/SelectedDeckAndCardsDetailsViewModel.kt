package com.example.flashcardapp.deck

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckNCardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SelectedDeckAndCardsDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val deckNCardRepository: DeckNCardRepository
) : ViewModel() {
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])

    val selectedDeckCardsUiState: StateFlow<SelectedDeckUiState> =
        deckNCardRepository.getCombinedDeckCardsStream(deckId).map {
            SelectedDeckUiState(it.deck, it.cards)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = SelectedDeckUiState(
                Deck(
                    deckId = 0,
                    name = "Loading",
                    description = "Loading",
                    numOfCards = 0
                )
            )
        )

    suspend fun updateDeckDetails(
        newName: String,
        newDescription: String,
        numOfCards: Int
    ) {
        deckNCardRepository.updateDeck(
            Deck(
                deckId = deckId,
                name = newName,
                description = newDescription,
                numOfCards = numOfCards
            )
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SelectedDeckUiState(
    val selectedDeck: Deck,
    val deckCards: List<Card> = listOf()
)