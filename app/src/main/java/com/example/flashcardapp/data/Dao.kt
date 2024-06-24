package com.example.flashcardapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)

    @Query("SELECT * FROM cards WHERE cardId = :cardId")
    fun getIndividualDeckCard(cardId: Int): Flow<Card>

    @Query("SELECT * FROM cards WHERE deckId = :deckId")
    fun getAllDeckCards(deckId: Int): Flow<List<Card>>
}

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(deck: Deck)

    @Update
    suspend fun update(deck: Deck)

    @Delete
    suspend fun delete(deck: Deck)

    @Query("SELECT * FROM decks WHERE deckId = :deckId")
    fun getIndividualDeck(deckId: Int): Flow<Deck>

    @Query("SELECT * FROM decks ORDER BY deckId DESC")
    fun getAllDecks(): Flow<List<Deck>>
}