package com.example.flashcardapp.card

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.data.CardConstant.CARDANSWERLENGTH
import com.example.flashcardapp.data.CardConstant.CARDQUESTIONLENGTH
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddNewCardScreen(
    topBar: @Composable () -> Unit,
    backgroundBrush: Brush,
    viewModel: AddNewCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    cancelCreateClicked: () -> Unit
) {
    val context = LocalContext.current

    var currentNewCardQuestion by rememberSaveable {
        mutableStateOf("")
    }
    var currentNewCardAnswer by rememberSaveable {
        mutableStateOf("")
    }

    var contentError by rememberSaveable {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    var cancelCreateNewCard by rememberSaveable {
        mutableStateOf(false)
    }

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
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Please enter your question and answer.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = currentNewCardQuestion,
                            onValueChange = { currentNewCardQuestion = it },
                            label = {
                                if (currentNewCardQuestion.length > CARDQUESTIONLENGTH) {
                                    contentError = true
                                    Text(
                                        text = "Question is too long!",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else if (currentNewCardQuestion.isEmpty()) {
                                    contentError = true
                                    Text(
                                        text = "Question cannot be empty.",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    contentError = false
                                    Text(
                                        text = "Question",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            isError = currentNewCardQuestion.length > CARDQUESTIONLENGTH || currentNewCardQuestion.isEmpty()
                        )
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = currentNewCardAnswer,
                            onValueChange = { currentNewCardAnswer = it },
                            keyboardActions = KeyboardActions(),
                            label = {
                                if (currentNewCardAnswer.length > CARDANSWERLENGTH) {
                                    contentError = true
                                    Text(
                                        text = "Answer is too long!",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else if (currentNewCardAnswer.isEmpty()) {
                                    contentError = true
                                    Text(
                                        text = "Answer cannot be empty.",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    contentError = false
                                    Text(
                                        text = "Answer",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            isError = currentNewCardAnswer.length > CARDANSWERLENGTH || currentNewCardAnswer.isEmpty()
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedButton(onClick = { cancelCreateNewCard = true }
                    ) {
                        Text(text = "Back to deck")
                    }
                    ElevatedButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.createNewCard(
                                    question = currentNewCardQuestion,
                                    answer = currentNewCardAnswer
                                )
                                delay(1000L)
                                currentNewCardQuestion = ""
                                currentNewCardAnswer = ""
                            }
                            Toast.makeText(context, "New card created!", LENGTH_SHORT).show()
                        },
                        enabled = !contentError
                    ) {
                        Text(text = "Create + Next")
                    }
                }
            }
            if (cancelCreateNewCard) {
                CancelCreateNewDeckDialog(
                    confirmCancelCreateNewDeckClicked = cancelCreateClicked,
                    dismissCancelCreateNewDeckClicked = { cancelCreateNewCard = false }
                )
            }
        }
    }
}

@Composable
fun CancelCreateNewDeckDialog(
    confirmCancelCreateNewDeckClicked: () -> Unit,
    dismissCancelCreateNewDeckClicked: () -> Unit
) {
    Dialog(onDismissRequest = dismissCancelCreateNewDeckClicked) {
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
                    text = "Confirm quitting creating new cards?",
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
                    ElevatedButton(onClick = dismissCancelCreateNewDeckClicked) {
                        Text("Back")
                    }
                    ElevatedButton(onClick = confirmCancelCreateNewDeckClicked) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}