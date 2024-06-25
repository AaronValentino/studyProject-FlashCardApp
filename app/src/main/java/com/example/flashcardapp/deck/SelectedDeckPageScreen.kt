package com.example.flashcardapp.deck

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.data.CardConstant.CARDANSWERLENGTH
import com.example.flashcardapp.data.CardConstant.CARDQUESTIONLENGTH
import com.example.flashcardapp.data.DeckConstant.DECKDESCRIPTIONLENGTH
import com.example.flashcardapp.data.DeckConstant.DECKNAMELENGTH
import com.example.flashcardapp.ui.theme.Shape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SelectedDeckPageScreen(
    backgroundBrush: Brush,
    onClickedBack: () -> Unit,
    onClickedAddNewCard: (Int, String) -> Unit,
    onClickedAllCards: () -> Unit,
    onClickedLesson: () -> Unit,
    cardsAdded: Int,
    viewModel: SelectedDeckAndCardsDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.selectedDeckCardsUiState.collectAsState()

    if (cardsAdded > 0) {
        viewModel.autoUpdateDeckDetails()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val infiniteRotationColor = infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.onPrimary,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    var editDeckDetails by rememberSaveable {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    if (editDeckDetails) {
        EditDeckDetails(
            confirmEditDeckDetailsClicked = { newTitle, newDescription ->
                coroutineScope.launch {
                    viewModel.updateDeckDetails(
                        newName = newTitle,
                        newDescription = newDescription,
                        numOfCards = uiState.value.deckCards.size
                    )
                    delay(1000L)
                    editDeckDetails = false
                }
            },
            dismissEditDeckDetailsClicked = {
                editDeckDetails = false
            },
            oldDeckName = uiState.value.selectedDeck.name,
            oldDeckDescription = uiState.value.selectedDeck.description
        )
    }

    var editCardDetails by rememberSaveable {
        mutableStateOf(false)
    }

    if (editCardDetails) {
        EditCardDetails(
            confirmEditCardDetailsClicked = {
                coroutineScope.launch {
                    viewModel.updateCardDetails()
                    delay(1000L)
                    editCardDetails = false
                }
            },
            dismissEditCardDetailsClicked = {
                editCardDetails = false
            },
            viewModel = viewModel
        )
    }

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
                        NoCardsAddNewCard(
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
                            },
                            onClickedEditCardDetails = {
                                viewModel.updateCardToBeEditedFullDetails(it)
                                editCardDetails = true
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
                onClick = { editDeckDetails = true },
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Deck Details Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EditDeckDetails(
    confirmEditDeckDetailsClicked: (String, String) -> Unit,
    dismissEditDeckDetailsClicked: () -> Unit,
    oldDeckName: String,
    oldDeckDescription: String
) {
    var editDeckName by rememberSaveable {
        mutableStateOf(oldDeckName)
    }
    var editDeckDescription by rememberSaveable {
        mutableStateOf(oldDeckDescription)
    }

    Dialog(onDismissRequest = dismissEditDeckDetailsClicked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .aspectRatio(0.7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                ) {
                    OutlinedTextField(
                        value = editDeckName,
                        onValueChange = { editDeckName = it },
                        label = {
                            if (editDeckName.length > DECKNAMELENGTH) {
                                Text(
                                    text = "New deck's name is too long!",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = "New deck's name",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        isError = editDeckName.length > DECKNAMELENGTH
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    OutlinedTextField(
                        value = editDeckDescription,
                        onValueChange = { editDeckDescription = it },
                        keyboardActions = KeyboardActions(),
                        label = {
                            if (editDeckDescription.length > DECKDESCRIPTIONLENGTH) {
                                Text(
                                    text= "New deck's description is too long!",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text= "New deck's description (Optional)",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        isError = editDeckDescription.length > DECKDESCRIPTIONLENGTH
                    )
                }
                Spacer(modifier = Modifier.weight(0.25f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedButton(onClick = dismissEditDeckDetailsClicked) {
                        Text("Cancel")
                    }
                    ElevatedButton(
                        onClick = {
                            confirmEditDeckDetailsClicked(editDeckName, editDeckDescription)
                        },
                        enabled = (
                                editDeckName.length <= DECKNAMELENGTH &&
                                editDeckName.isNotEmpty() &&
                                editDeckDescription.length <= DECKDESCRIPTIONLENGTH &&
                                editDeckDescription.isNotEmpty() &&
                                ((editDeckName != oldDeckName) || (editDeckDescription != oldDeckDescription))
                        )
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@Composable
fun NoCardsAddNewCard(
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
    onClickedAddNewCard: () -> Unit,
    onClickedEditCardDetails: (Card) -> Unit
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
                    onClick = { onClickedEditCardDetails(it) },
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

@Composable
fun EditCardDetails(
    confirmEditCardDetailsClicked: () -> Unit,
    dismissEditCardDetailsClicked: () -> Unit,
    viewModel: SelectedDeckAndCardsDetailsViewModel
) {
    val currentCardState = viewModel.selectedCardDetailsUiState.collectAsState()

    Log.d("CheckCard", "$currentCardState")

    var editCardQuestion by rememberSaveable {
        mutableStateOf(currentCardState.value.question)
    }
    var editCardAnswer by rememberSaveable {
        mutableStateOf(currentCardState.value.answer)
    }

    Dialog(onDismissRequest = dismissEditCardDetailsClicked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .aspectRatio(0.7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                ) {
                    OutlinedTextField(
                        value = editCardQuestion,
                        onValueChange = { editCardQuestion = it },
                        label = {
                            if (editCardQuestion.length > CARDQUESTIONLENGTH) {
                                Text(
                                    text = "The card's new question is too long!",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = "Card's new question",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        isError = editCardQuestion.length > CARDQUESTIONLENGTH
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    OutlinedTextField(
                        value = editCardAnswer,
                        onValueChange = { editCardAnswer = it },
                        keyboardActions = KeyboardActions(),
                        label = {
                            if (editCardAnswer.length > CARDANSWERLENGTH) {
                                Text(
                                    text= "Card's new answer is too long!",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text= "Card's new answer",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        isError = editCardAnswer.length > CARDANSWERLENGTH
                    )
                }
                Spacer(modifier = Modifier.weight(0.25f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedButton(onClick = dismissEditCardDetailsClicked) {
                        Text("Cancel")
                    }
                    ElevatedButton(
                        onClick = {
                            viewModel.updateCardToBeEditedDetails(editCardQuestion, editCardAnswer)
                            confirmEditCardDetailsClicked()
                        },
                        enabled = (
                                editCardQuestion.length <= CARDQUESTIONLENGTH &&
                                editCardQuestion.isNotEmpty() &&
                                editCardAnswer.length <= CARDANSWERLENGTH &&
                                editCardAnswer.isNotEmpty() &&
                                ((editCardQuestion != currentCardState.value.question) || (editCardAnswer != currentCardState.value.answer))
                        )
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

enum class QuestionAnswer {
    QUESTION, ANSWER
}