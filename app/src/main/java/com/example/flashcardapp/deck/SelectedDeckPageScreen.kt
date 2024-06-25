package com.example.flashcardapp.deck

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.ui.theme.Shape

@Composable
fun SelectedDeckPageScreen(
    backgroundBrush: Brush,
    onClickedBack: () -> Unit,
    onClickedEditDeck: () -> Unit,
    onClickedAddNewCard: (Int, String) -> Unit,
    onClickedAllCards: () -> Unit,
    onClickedLesson: () -> Unit,
    viewModel: SelectedDeckAndCardsDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.selectedDeckCardsUiState.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val infiniteRotationColor = infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.onPrimary,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(backgroundBrush)
                .fillMaxSize()
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.value.selectedDeck.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "${uiState.value.deckCards.size} Card(s)",
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f),
                        border = BorderStroke(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Deck's descriptions: ",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 16.dp,
                                    end = 16.dp,
                                    start = 16.dp
                                )
                        )
                        Text(
                            text = uiState.value.selectedDeck.description,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (uiState.value.deckCards.isEmpty()) {
                        AddNewCard(
                            infiniteRotationColor = infiniteRotationColor,
                            onClickedAddNewCard = {
                                onClickedAddNewCard(
                                    uiState.value.selectedDeck.deckId,
                                    uiState.value.selectedDeck.name
                                )
                            }
                        )
                    } else {
                        GenerateLazyRowForCards(
                            infiniteRotationColor = infiniteRotationColor,
                            uiState = uiState,
                            onClickedAddNewCard = {
                                onClickedAddNewCard(
                                    uiState.value.selectedDeck.deckId,
                                    uiState.value.selectedDeck.name
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ElevatedButton(
                            onClick = onClickedAllCards,
                            border = BorderStroke(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "All cards",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        ElevatedButton(
                            onClick = onClickedLesson,
                            border = BorderStroke(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "Lesson",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = onClickedBack,
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            FloatingActionButton(
                onClick = onClickedEditDeck,
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Close Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AddNewCard(
    infiniteRotationColor: State<Color>,
    onClickedAddNewCard: () -> Unit
) {
    AnimatedContent(targetState = infiniteRotationColor, label = "") {
        Card(
            modifier = Modifier
                .clickable { onClickedAddNewCard() }
                .fillMaxHeight(0.8f)
                .fillMaxWidth(0.7f)
                .padding(vertical = 16.dp),
            border = BorderStroke(
                width = 4.dp,
                color = it.value
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add cards",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            )
        }
    }
}

@Composable
fun GenerateLazyRowForCards(
    infiniteRotationColor: State<Color>,
    uiState: State<SelectedDeckUiState>,
    onClickedAddNewCard: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f),
        contentPadding = PaddingValues(
            vertical = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item("add_new_card") {
            AnimatedContent(targetState = infiniteRotationColor, label = "") {
                Card(
                    modifier = Modifier
                        .clickable { onClickedAddNewCard() }
                        .fillMaxHeight(0.8f)
                        .aspectRatio(0.2f),
                    border = BorderStroke(
                        width = 2.dp,
                        color = it.value
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add cards",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
        items(uiState.value.deckCards) {
            val questionAnswer: Map<QuestionAnswer, String> = mapOf(
                QuestionAnswer.QUESTION to it.question,
                QuestionAnswer.ANSWER to it.answer
            )

            var contentDisplay by rememberSaveable {
                mutableStateOf(QuestionAnswer.QUESTION)
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = contentDisplay,
                    label = "",
                    transitionSpec = {
                        (
                            slideInVertically()
                            +
                            fadeIn(animationSpec = tween(500))
                            +
                            scaleIn(initialScale = 0.5f, animationSpec = tween(500))
                        )
                        .togetherWith(
                            slideOutVertically { x -> x }
                            +
                            fadeOut(animationSpec = tween(500))
                            +
                            scaleOut(targetScale = 0.5f, animationSpec = tween(500))
                        )
                    },
                ) { questionOrAnswer ->
                    Card(
                        modifier = Modifier
                            .clickable {
                                contentDisplay = if (contentDisplay == QuestionAnswer.QUESTION) {
                                    QuestionAnswer.ANSWER
                                } else {
                                    QuestionAnswer.QUESTION
                                }
                            }
                            .fillMaxSize(0.8f)
                            .aspectRatio(0.85f)
                            .padding(horizontal = 20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (contentDisplay == QuestionAnswer.QUESTION) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${questionAnswer[questionOrAnswer]}",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                FloatingActionButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(start = 4.dp),
                    shape = Shape.small
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Close Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

enum class QuestionAnswer {
    QUESTION, ANSWER
}