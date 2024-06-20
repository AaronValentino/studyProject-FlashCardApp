package com.example.flashcardapp.deck

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.flashcardapp.card.Card
import kotlinx.coroutines.flow.StateFlow

class DeckListViewModel() : ViewModel() {
}

data class DeckListUiState(val listItem: List<Card> = listOf())