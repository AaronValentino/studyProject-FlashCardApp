package com.example.flashcardapp.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode.Companion.Softlight
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.data.Card
import com.example.flashcardapp.deck.SelectedDeckAndCardsDetailsViewModel
import com.example.flashcardapp.ui.theme.Shape
import com.example.flashcardapp.ui.theme.getCircleBrush
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AllCardsScreen(
    topBar: @Composable () -> Unit,
    backgroundBrush: Brush,
    onClickedGoToLessons: () -> Unit,
    viewModel: SelectedDeckAndCardsDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val allCards = viewModel.selectedDeckCardsUiState.collectAsState().value.deckCards

    val coroutineScope = rememberCoroutineScope()

    var editCardDetails by remember {
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

    var showConfirmDeleteAllCardsDialog by remember {
        mutableStateOf(false)
    }

    if (showConfirmDeleteAllCardsDialog) {
        ConfirmDeleteAllCardsDialog(
            confirmDeleteAllCardsClicked = {
                coroutineScope.launch {
                    viewModel.deleteAllCards()
                    delay(1000L)
                    showConfirmDeleteAllCardsDialog = false
                }
            },
            dismissDeleteAllCardsClicked = {
                showConfirmDeleteAllCardsDialog = false
            }
        )
    }

    var expandAll by rememberSaveable {
        mutableStateOf(false)
    }

    var editMode by rememberSaveable {
        mutableStateOf(false)
    }

    val circleBrush = getCircleBrush()
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val infiniteChangingNumX1 = infiniteTransition.animateValue(
        initialValue = 0f,
        targetValue = 1000f,
        typeConverter = Float.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(20000),
            repeatMode = RepeatMode.Reverse
        )
        , label = ""
    )
    val infiniteChangingNumX2 = infiniteTransition.animateValue(
        initialValue = -1f,
        targetValue = 1f,
        typeConverter = Float.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(17500),
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
            animation = tween(10000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Scaffold(
        topBar = topBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .drawBehind {
                    val radius = infiniteChangingNumOffset.value * size.minDimension / 250
                    val x = (size.width/2) + (infiniteChangingNumX1.value*infiniteChangingNumX2.value - infiniteChangingNumOffset.value)
                    val y = (size.height/2) + (infiniteChangingNumX1.value*infiniteChangingNumX2.value - infiniteChangingNumOffset.value)
                    drawCircle(
                        brush = circleBrush,
                        radius = radius,
                        center = Offset(
                            x = x,
                            y = y
                        ),
                        blendMode = Softlight
                    )
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (allCards.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "The deck is empty!",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(top = 40.dp)
                        )
                    }
                } else {
                    AnimatedContent(targetState = expandAll, label = "") {
                        GenerateCards(
                            onClickedDeleteAllCards = { showConfirmDeleteAllCardsDialog = !showConfirmDeleteAllCardsDialog },
                            allCards = allCards,
                            onClickedEditButton = { card ->
                                viewModel.setSelectedCardFullDetails(card)
                                editCardDetails = true
                            },
                            editMode = editMode,
                            expandAll = it
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.fillMaxSize(0.9f))
                    GenerateButtonsRow(
                        onClickedEditMode = { editMode = !editMode },
                        onClickedGoToLessons = onClickedGoToLessons,
                        onClickedExpandAll = {
                            expandAll = !expandAll
                        },
                        expandAll = expandAll
                    )
                }
            }
        }
    }
}

@Composable
fun GenerateCards(
    onClickedDeleteAllCards: () -> Unit,
    allCards: List<Card>,
    onClickedEditButton: (Card) -> Unit,
    editMode: Boolean,
    expandAll: Boolean,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(allCards) {
            var showAnswer by rememberSaveable {
                mutableStateOf(expandAll)
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .clickable { showAnswer = !showAnswer }
                        .fillMaxSize(0.9f)
                        .animateContentSize()
                        .aspectRatio(if (showAnswer) 0.5f else 0.8f)
                        .padding(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(if (showAnswer) 0.5f else 1f)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = it.question,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 24.sp
                            )
                        }
                        if (showAnswer) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = it.answer,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 24.sp,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                }
                if (editMode) {
                    FloatingActionButton(
                        onClick = { onClickedEditButton(it) },
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

        if (editMode) {
            item("delete_all_cards") {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .clickable { onClickedDeleteAllCards() }
                            .fillMaxSize(0.9f)
                            .aspectRatio(0.8f)
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Delete all cards",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "🗑️",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        for (x in 1..2) {
            item("bottom_space$x") {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.5f)
                )
            }
        }
    }
}

@Composable
fun GenerateButtonsRow(
    onClickedEditMode: () -> Unit,
    onClickedGoToLessons: () -> Unit,
    onClickedExpandAll: () -> Unit,
    expandAll: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val infiniteChangingElevation = infiniteTransition.animateValue(
        initialValue = 0.dp,
        targetValue = 20.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GenerateButtons(
            displayText1 = "Edit Mode",
            displayText2 = "✍️",
            modifier = Modifier.weight(1f),
            onClicked = onClickedEditMode
        )
        GenerateButtons(
            displayText1 = "Lessons",
            displayText2 = "📖",
            modifier = Modifier.weight(1f),
            onClicked = onClickedGoToLessons,
            elevationToSet = infiniteChangingElevation.value
        )
        GenerateButtons(
            displayText1 = if (!expandAll) "Expand all" else "Collapse all",
            displayText2 = if (!expandAll) "🔽" else "🔼",
            modifier = Modifier.weight(1f),
            onClicked = onClickedExpandAll
        )
    }
}

@Composable
fun GenerateButtons(
    displayText1: String,
    displayText2: String,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    elevationToSet: Dp = 10.dp
) {
    ElevatedButton(
        onClick = onClicked,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight(),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = elevationToSet
        )
    ) {
        Column {
            Text(
                text = displayText1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            if (displayText2.isNotEmpty()) {
                Text(
                    text = displayText2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteAllCardsDialog(
    confirmDeleteAllCardsClicked: () -> Unit,
    dismissDeleteAllCardsClicked: () -> Unit
) {
    Dialog(onDismissRequest = dismissDeleteAllCardsClicked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Confirm delete all cards?",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedButton(onClick = dismissDeleteAllCardsClicked) {
                        Text("Cancel")
                    }
                    ElevatedButton(onClick = confirmDeleteAllCardsClicked) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}