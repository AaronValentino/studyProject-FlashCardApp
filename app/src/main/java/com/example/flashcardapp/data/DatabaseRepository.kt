package com.example.flashcardapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface DeckNCardRepository {

    suspend fun insertCard(card: Card)

    suspend fun updateCard(card: Card)

    suspend fun deleteCard(card: Card)

    fun getIndividualDeckCardStream(cardId: Int): Flow<Card>

    fun getAllDeckCardsStream(deckId: Int): Flow<List<Card>>

    suspend fun insertDeck(deck: Deck)

    suspend fun updateDeck(deck: Deck)

    suspend fun deleteDeck(deck: Deck)

    fun getIndividualDeckStream(deckId: Int): Flow<Deck>

    fun getAllDecksStream(): Flow<List<Deck>>

    fun getCombinedDeckCardsStream(deckId: Int): Flow<DeckCards>
}

class LocalDeckNCardRepository(
    private val cardDao: CardDao, private val deckDao: DeckDao
) : DeckNCardRepository {
    override suspend fun insertCard(card: Card) = cardDao.insert(card)

    override suspend fun updateCard(card: Card) = cardDao.update(card)

    override suspend fun deleteCard(card: Card) = cardDao.delete(card)

    override fun getIndividualDeckCardStream(cardId: Int): Flow<Card> = cardDao.getIndividualDeckCard(cardId)

    override fun getAllDeckCardsStream(deckId: Int): Flow<List<Card>> = cardDao.getAllDeckCards(deckId)

    override suspend fun insertDeck(deck: Deck) = deckDao.insert(deck)

    override suspend fun updateDeck(deck: Deck) = deckDao.update(deck)

    override suspend fun deleteDeck(deck: Deck) = deckDao.delete(deck)

    override fun getIndividualDeckStream(deckId: Int): Flow<Deck> = deckDao.getIndividualDeck(deckId)

    override fun getAllDecksStream(): Flow<List<Deck>> =deckDao.getAllDecks()

    override fun getCombinedDeckCardsStream(deckId: Int): Flow<DeckCards> {
        val deckFlow = getIndividualDeckStream(deckId)
        val cardsFlow = getAllDeckCardsStream(deckId)

        return deckFlow.combine(cardsFlow) { deck, cards ->
            DeckCards(deck, cards)
        }
    }
}

data class DeckCards(
    val deck: Deck,
    val cards: List<Card>
)