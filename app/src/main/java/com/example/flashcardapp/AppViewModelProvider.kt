package com.example.flashcardapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcardapp.card.AddNewCardViewModel
import com.example.flashcardapp.deck.AddNewDeckViewModel
import com.example.flashcardapp.deck.DeckListViewModel
import com.example.flashcardapp.deck.SelectedDeckAndCardsDetailsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DeckListViewModel(flashardApplication().container.deckNCardRepository)
        }

        initializer {
            SelectedDeckAndCardsDetailsViewModel(
                this.createSavedStateHandle(),
                flashardApplication().container.deckNCardRepository
            )
        }

        initializer {
            AddNewDeckViewModel(
                flashardApplication().container.deckNCardRepository
            )
        }

        initializer {
            AddNewCardViewModel(
                this.createSavedStateHandle(),
                flashardApplication().container.deckNCardRepository
            )
        }
    }
}

fun CreationExtras.flashardApplication(): FlashardApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlashardApplication)