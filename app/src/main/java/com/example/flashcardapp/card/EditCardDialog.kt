package com.example.flashcardapp.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flashcardapp.data.CardConstant
import com.example.flashcardapp.deck.SelectedDeckAndCardsDetailsViewModel

@Composable
fun EditCardDialog(
    confirmEditCardDetailsClicked: () -> Unit,
    dismissEditCardDetailsClicked: () -> Unit,
    confirmDeleteCardClicked: () -> Unit,
    viewModel: SelectedDeckAndCardsDetailsViewModel
) {
    val currentCardState = viewModel.selectedCardDetailsUiState.collectAsState()

    var editCardQuestion by rememberSaveable {
        mutableStateOf(currentCardState.value.question)
    }
    var editCardAnswer by rememberSaveable {
        mutableStateOf(currentCardState.value.answer)
    }

    var showConfirmDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showConfirmDeleteDialog) {
        ConfirmDeleteCardDialog(
            confirmDeleteCardClicked = {
                showConfirmDeleteDialog = false
                confirmDeleteCardClicked()
            },
            dismissDeleteCardClicked = { showConfirmDeleteDialog = false }
        )
    }

    Dialog(onDismissRequest = dismissEditCardDetailsClicked) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.5f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .align(alignment = Alignment.Center)
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
                                if (editCardQuestion.length > CardConstant.CARDQUESTIONLENGTH) {
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
                            isError = editCardQuestion.length > CardConstant.CARDQUESTIONLENGTH
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
                                if (editCardAnswer.length > CardConstant.CARDANSWERLENGTH) {
                                    Text(
                                        text = "Card's new answer is too long!",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    Text(
                                        text = "Card's new answer",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            isError = editCardAnswer.length > CardConstant.CARDANSWERLENGTH
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
                                viewModel.updateSelectedCardNewDetailsTemp(
                                    editCardQuestion,
                                    editCardAnswer
                                )
                                confirmEditCardDetailsClicked()
                            },
                            enabled = (
                                    editCardQuestion.length <= CardConstant.CARDQUESTIONLENGTH &&
                                            editCardQuestion.isNotEmpty() &&
                                            editCardAnswer.length <= CardConstant.CARDANSWERLENGTH &&
                                            editCardAnswer.isNotEmpty() &&
                                            ((editCardQuestion != currentCardState.value.question) || (editCardAnswer != currentCardState.value.answer))
                                    )
                        ) {
                            Text("Done")
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = { showConfirmDeleteDialog = true },
                modifier = Modifier.align(alignment = Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Card Button",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteCardDialog(
    confirmDeleteCardClicked: () -> Unit,
    dismissDeleteCardClicked: () -> Unit
) {
    Dialog(onDismissRequest = dismissDeleteCardClicked) {
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
                    text = "Confirm delete this card?",
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
                    ElevatedButton(onClick = dismissDeleteCardClicked) {
                        Text("Cancel")
                    }
                    ElevatedButton(onClick = confirmDeleteCardClicked) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}