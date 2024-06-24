package com.example.flashcardapp.data

import android.content.Context

interface AppContainer {
    val deckNCardRepository: DeckNCardRepository
}

// [AppContainer] implementation that provides instance of [LocalDeckNCardRepository]
class AppDataContainer(private val context: Context) : AppContainer {
    override val deckNCardRepository: DeckNCardRepository by lazy {
        val databaseInstance = FlashardDatabase.getDatabase(context)
        LocalDeckNCardRepository(databaseInstance.cardDao(), databaseInstance.deckDao())
    }
}