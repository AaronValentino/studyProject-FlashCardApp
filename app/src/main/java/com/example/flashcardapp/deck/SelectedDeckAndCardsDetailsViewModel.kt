package com.example.flashcardapp.deck

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckNCardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    init {
        autoUpdateDeckDetails()
    }

    private fun autoUpdateDeckDetails() {
        viewModelScope.launch{
            delay(1000L)
            selectedDeckCardsUiState.value.let {
                Log.d("viewModelScopeToUpdateDeck", "Launched")
                updateDeckDetails(
                    it.selectedDeck.name,
                    it.selectedDeck.description,
                    it.deckCards.size
                )
            }
        }
    }

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

    private val _selectedCardDetailsUiState = MutableStateFlow(
        Card(
            cardId = 0,
            deckId = 0,
            question = "Loading",
            answer = "Loading",
            numOfTimePracticed = 0,
            lastAnsweredCorrect = false
        )
    )
    val selectedCardDetailsUiState: StateFlow<Card> = _selectedCardDetailsUiState.asStateFlow()

    fun updateCardToBeEditedFullDetails(currentCard: Card) {
        _selectedCardDetailsUiState.update { currentState ->
            currentState.copy(
                deckId = currentCard.deckId,
                cardId = currentCard.cardId,
                question = currentCard.question,
                answer = currentCard.answer,
                numOfTimePracticed = currentCard.numOfTimePracticed,
                lastAnsweredCorrect = currentCard.lastAnsweredCorrect
            )
        }
    }

    fun updateCardToBeEditedDetails(question: String, answer: String) {
        Log.d("CheckUpdateCard", "updateCardToBeEditedDetails")
        _selectedCardDetailsUiState.update { currentState ->
            currentState.copy(
                question = question,
                answer = answer
            )
        }
    }

    suspend fun updateCardDetails() {
        Log.d("CheckUpdateCard", "updateCardDetails")
        deckNCardRepository.updateCard(selectedCardDetailsUiState.value)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SelectedDeckUiState(
    val selectedDeck: Deck,
    val deckCards: List<Card> = listOf()
)