package com.example.flashcardapp.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckNCardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DeckListViewModel(deckNCardRepository: DeckNCardRepository) : ViewModel() {
    val deckListUiState: StateFlow<DeckListUiState> =
        deckNCardRepository.getAllDecksStream().map {
            DeckListUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DeckListUiState()
        )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DeckListUiState(
    val listItem: List<Deck> = listOf()
)