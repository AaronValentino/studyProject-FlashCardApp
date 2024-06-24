package com.example.flashcardapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true) val deckId: Int = 0,
    val name: String,
    val description: String,
    val numOfCards: Int
)

//private var deckCount = 0
//object Deck1 {
//    val deck: Deck = createDeck()
//}
//
//object Deck2 {
//    val deck: Deck = createDeck()
//}
//
//object Deck3 {
//    val deck: Deck = createDeck()
//}
//
//fun createDeck(): Deck {
//    deckCount++
//    val deckID = deckCount
//
//    return Deck(deckID, "Deck $deckID", createCardsData(30))
//}
