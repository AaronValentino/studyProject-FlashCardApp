package com.example.flashcardapp.deck

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import androidx.compose.material3.MaterialTheme as MaterialTheme1

@Composable
fun DeckListScreen(
    topBar: @Composable () -> Unit,
    backgroundBrush: Brush,
    cardClicked: (Int) -> Unit,
    addNewDeckClicked: (Int) -> Unit,
    viewModel: DeckListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.deckListUiState.collectAsState()
    val deckSize = (uiState.value.listItem.size + 1)
    Log.d("Check Deck Size", deckSize.toString())

    Scaffold(
        topBar = topBar
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(brush = backgroundBrush)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(uiState.value.listItem) {
                    GenerateCard(
                        deckName = it.name,
                        numOfCards = it.numOfCards,
                        cardClicked = { cardClicked(it.deckId) }
                    )
                }

                item {
                    GenerateCardAddNewDeck(
                        addNewDeckClicked = { addNewDeckClicked(deckSize) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GenerateCard(
    deckName: String,
    numOfCards: Int,
    cardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(0.75f),
        border = BorderStroke(
            width = 4.dp,
            color = MaterialTheme1.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = cardClicked)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Remove this if the lines of code below are reactivated
        ) {
            Text(
                text = deckName,
                style = MaterialTheme1.typography.displaySmall,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (numOfCards == 0 || numOfCards == 1) "$numOfCards card" else "$numOfCards cards",
                style = MaterialTheme1.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun GenerateCardAddNewDeck(
    addNewDeckClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(0.75f),
        border = BorderStroke(
            width = 4.dp,
            color = MaterialTheme1.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = addNewDeckClicked)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "null",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme1.colorScheme.primary
            )
        }
    }
}