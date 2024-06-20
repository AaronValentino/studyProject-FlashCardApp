package com.example.flashcardapp.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.card.CardsData

@Composable
fun DeckListScreen(onClicked: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            items(CardsData.deck) {
                var print by rememberSaveable {
                    mutableStateOf(it.question)
                }
                val cardColor = if (print == it.question)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer

                GenerateCard(
                    print = print,
                    cardClicked = {
                        print = if (print == it.question) it.answer else it.question
                    },
                    modifier = Modifier
                        .background(color = cardColor)
                )
            }
        }
    }
}

@Composable
private fun GenerateCard(
    print: String,
    cardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .aspectRatio(1f)
    ) {
        Column(
            modifier = modifier
                .clickable(onClick = cardClicked)
                .fillMaxSize()
                .wrapContentSize()
        ) {
            Text(
                text = print,
            )
        }
    }
}