package com.example.flashcardapp.deck

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.R
import com.example.flashcardapp.card.EditCardDialog
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.ui.theme.Shape
import com.example.flashcardapp.ui.theme.getCircleBrush
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SelectedDeckPageScreen(
    backgroundBrush: Brush,
    onClickedBack: () -> Unit,
    onClickedConfirmDeleteDeck: () -> Unit,
    onClickedAddNewCard: (Int, String) -> Unit,
    onClickedAllCards: (Int, String) -> Unit,
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

    var editDeckDetails by rememberSaveable {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    if (editDeckDetails) {
        EditDeckDialog(
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
            confirmDeleteDeckClicked = {
                coroutineScope.launch {
                    viewModel.confirmDeleteDeck()
                    onClickedConfirmDeleteDeck()
                }
            },
            currentDeck = uiState.value.selectedDeck
        )
    }

    var editCardDetails by rememberSaveable {
        mutableStateOf(false)
    }

    if (editCardDetails) {
        EditCardDialog(
            confirmEditCardDetailsClicked = {
                coroutineScope.launch {
                    viewModel.updateSelectedCardDetailsToDatabase()
                    delay(1000L)
                    editCardDetails = false
                }
            },
            dismissEditCardDetailsClicked = {
                editCardDetails = false
            },
            confirmDeleteCardClicked = {
                coroutineScope.launch {
                    viewModel.deleteSelectedCard()
                }
                editCardDetails = false
            },
            viewModel = viewModel
        )
    }

    var reverseOrder by rememberSaveable {
        mutableStateOf(false)
    }

    val circleBrush = getCircleBrush()
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

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(backgroundBrush)
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
                }
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
                        .padding(
                            vertical = 20.dp,
                            horizontal = 16.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.value.selectedDeck.name,
                        style = MaterialTheme.typography.titleLarge,
                        lineHeight = 40.sp,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .aspectRatio(3.5f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = uiState.value.deckCards.size.let{
                            if (it <= 1) stringResource(id = R.string.quantity_card, it)
                            else stringResource(id = R.string.quantity_cards, it)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.75f),
                        border = BorderStroke(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        uiState.value.selectedDeck.description.let {
                            if (it.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.no_deck_description),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(0.90f),
                        contentAlignment = Alignment.Center
                    ) {
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
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row (
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    AnimatedContent(
                                        targetState = infiniteRotationColor,
                                        label = ""
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .clickable {
                                                    onClickedAddNewCard(
                                                        uiState.value.selectedDeck.deckId,
                                                        uiState.value.selectedDeck.name
                                                    )
                                                }
                                                .fillMaxWidth(0.5f)
                                                .aspectRatio(4f),
                                            border = BorderStroke(
                                                width = 2.dp,
                                                color = it.value
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = stringResource(id = R.string.contentDescription_add_cards),
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp)
                                            )
                                        }
                                    }
                                    Card(
                                        modifier = Modifier
                                            .clickable { reverseOrder = !reverseOrder }
                                            .fillMaxWidth()
                                            .aspectRatio(4f)
                                            .padding(horizontal = 16.dp),
                                        border = BorderStroke(
                                            width = 2.dp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Icon(
                                            imageVector =
                                                if (!reverseOrder) Icons.AutoMirrored.Filled.KeyboardArrowLeft
                                                else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = stringResource(id = R.string.contentDescription_change_cards_ordering),
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp)
                                        )
                                    }
                                }
                                GenerateLazyRowForCards(
                                    uiState = uiState,
                                    onClickedEditCardDetails = {
                                        viewModel.setSelectedCardFullDetails(it)
                                        editCardDetails = true
                                    },
                                    reverseOrder = reverseOrder
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ElevatedButton(
                            modifier = Modifier.sizeIn(minHeight = 200.dp),
                            onClick = {
                                onClickedAllCards(
                                    uiState.value.selectedDeck.deckId,
                                    uiState.value.selectedDeck.name
                                )
                            },
                            border = BorderStroke(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.button_all_cards),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        ElevatedButton(
                            modifier = Modifier.sizeIn(minHeight = 200.dp),
                            onClick = onClickedLesson,
                            border = BorderStroke(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.button_lesson),
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
                    contentDescription = stringResource(id = R.string.contentDescription_button_close),
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
                    contentDescription = stringResource(id = R.string.contentDescription_button_edit_deck_details),
                    tint = MaterialTheme.colorScheme.primary
                )
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
                contentDescription = stringResource(id = R.string.contentDescription_add_cards),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            )
        }
    }
}

@Composable
fun GenerateLazyRowForCards(
    uiState: State<SelectedDeckUiState>,
    onClickedEditCardDetails: (Card) -> Unit,
    reverseOrder: Boolean = false
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f),
        contentPadding = PaddingValues(
            top = 24.dp,
            bottom = 4.dp
        ),
        reverseLayout = reverseOrder,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                            .fillMaxSize(0.9f)
                            .aspectRatio(0.75f)
                            .padding(horizontal = 8.dp),
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
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 24.sp
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
                        contentDescription = stringResource(id = R.string.contentDescription_button_close),
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