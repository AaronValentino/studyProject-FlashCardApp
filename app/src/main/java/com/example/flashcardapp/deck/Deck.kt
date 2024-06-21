package com.example.flashcardapp.deck

import com.example.flashcardapp.card.Card
import com.example.flashcardapp.card.createCardsData

private var deckCount = 0

data class Deck(
    val deckID: Int,
    val deckName: String,
    val deckCards: List<Card>
)

object Deck1 {
    val deck: Deck = createDeck()
}

object Deck2 {
    val deck: Deck = createDeck()
}

object Deck3 {
    val deck: Deck = createDeck()
}

fun createDeck(): Deck {
    deckCount++
    val deckID = deckCount

    return Deck(deckID, "Deck $deckID", createCardsData(30))
}
