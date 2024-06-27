package com.example.flashcardapp.deck

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckNCardRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectedDeckAndCardsDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val deckNCardRepository: DeckNCardRepository
) : ViewModel() {
    private var deckStatus = true
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])

    val selectedDeckCardsUiState: StateFlow<SelectedDeckUiState> =
        deckNCardRepository.getCombinedDeckCardsStream(deckId).filterNotNull().map {
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
        deckStatus = true
        autoUpdateDeckDetails()
    }

    private fun autoUpdateDeckDetails() {
        viewModelScope.launch{
            delay(1000L)
            selectedDeckCardsUiState.value.let {
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

    suspend fun confirmDeleteDeck() {
        viewModelScope.cancel()
        val deckToBeDeleted: Deck = selectedDeckCardsUiState.value.selectedDeck.let{
            Deck(
                deckId = it.deckId,
                name = it.name,
                description = it.description,
                numOfCards = it.numOfCards
            )
        }
        deckNCardRepository.deleteDeck(deckToBeDeleted)
    }

    // Set up for card selected to be edited
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

    // For the dialog to get the card's details before editing
    fun inputCardToBeEditedFullDetails(currentCard: Card) {
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

    // Input the new question and answer into the state for updating
    fun updateCardToBeEditedDetails(question: String, answer: String) {
        _selectedCardDetailsUiState.update { currentState ->
            currentState.copy(
                question = question,
                answer = answer
            )
        }
    }

    suspend fun updateCardDetails() {
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