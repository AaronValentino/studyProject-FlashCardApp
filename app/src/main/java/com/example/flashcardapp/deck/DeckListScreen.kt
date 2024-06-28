package com.example.flashcardapp.deck

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.R
import com.example.flashcardapp.ui.theme.getCircleBrush
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

    val circleBrush = getCircleBrush()
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val infiniteChangingNumX1 = infiniteTransition.animateValue(
        initialValue = 0f,
        targetValue = 1000f,
        typeConverter = Float.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 20000,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
        , label = ""
    )
    val infiniteChangingNumX2 = infiniteTransition.animateValue(
        initialValue = -1f,
        targetValue = 1f,
        typeConverter = Float.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 14441,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
        , label = ""
    )
    val ranNum1 = (1..100).random().toFloat()
    val ranNum2 = (1..50).random().toFloat()
    val infiniteChangingNumOffset = infiniteTransition.animateValue(
        initialValue = ranNum1 - ranNum2,
        targetValue = ranNum1 + ranNum2,
        typeConverter = Float.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 10000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Scaffold(
        topBar = topBar
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .drawBehind {
                    val radius = infiniteChangingNumOffset.value * size.minDimension / 250
                    val x =
                        (size.width / 2) + (infiniteChangingNumX1.value * infiniteChangingNumX2.value - infiniteChangingNumOffset.value)
                    val y =
                        (size.height / 2) + (infiniteChangingNumX1.value * infiniteChangingNumX2.value - infiniteChangingNumOffset.value)
                    drawCircle(
                        brush = circleBrush,
                        radius = radius,
                        center = Offset(
                            x = x,
                            y = y
                        ),
                        blendMode = BlendMode.Softlight
                    )
                },
            contentAlignment = Alignment.Center
        ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.fillMaxWidth()
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = deckName,
                    style = MaterialTheme1.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-1).sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =
                if (numOfCards <= 1) stringResource(id = R.string.quantity_card, numOfCards)
                else stringResource(id = R.string.quantity_cards, numOfCards) ,
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
                contentDescription = stringResource(id = R.string.contentDescription_create_new_deck),
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme1.colorScheme.primary
            )
        }
    }
}