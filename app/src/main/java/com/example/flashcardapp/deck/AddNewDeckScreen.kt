package com.example.flashcardapp.deck

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.AppViewModelProvider
import com.example.flashcardapp.R
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckConstant.DECKDESCRIPTIONLENGTH
import com.example.flashcardapp.data.DeckConstant.DECKNAMELENGTH
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddNewDeckScreen(
    topBar: @Composable () -> Unit,
    backgroundBrush: Brush,
    newDeckCreateClicked: (Int) -> Unit,
    viewModel: AddNewDeckViewModel = viewModel(factory = AppViewModelProvider.Factory),
    newDeckId: Int,
    cancelCreateClicked: () -> Unit
) {
    var deckName by rememberSaveable {
        mutableStateOf("New Deck ${(1..999).random()}")
    }
    var deckDescription by rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()

    var createNewDeck by rememberSaveable {
        mutableStateOf(false)
    }
    var cancelCreateNewDeck by rememberSaveable {
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
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                ) {
                    OutlinedTextField(
                        value = deckName,
                        onValueChange = { deckName = it },
                        label = {
                            if (deckName.length > DECKNAMELENGTH) {
                                Text(
                                    text = stringResource(id = R.string.textfield_label_deck_name_too_long),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else if (deckName.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.textfield_label_deck_name_is_empty),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = stringResource(id = R.string.textfield_label_deck_name),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        isError = deckName.length > DECKNAMELENGTH
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    OutlinedTextField(
                        value = deckDescription,
                        onValueChange = { deckDescription = it },
                        keyboardActions = KeyboardActions(),
                        label = {
                            if (deckDescription.length > DECKDESCRIPTIONLENGTH) {
                                Text(
                                    text = stringResource(id = R.string.textfield_label_deck_description_too_long),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = stringResource(id = R.string.textfield_label_deck_description),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        isError = deckDescription.length > DECKDESCRIPTIONLENGTH
                    )
                }
                Spacer(modifier = Modifier.weight(2f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedButton(onClick = { cancelCreateNewDeck = true }) {
                        Text(text = stringResource(id = R.string.button_cancel))
                    }
                    ElevatedButton(
                        onClick = { createNewDeck = true },
                        enabled = (deckName.length <= DECKNAMELENGTH && deckDescription.length <= DECKDESCRIPTIONLENGTH)
                    ) {
                        Text(text = stringResource(id = R.string.button_create))
                    }
                }
            }
            if (createNewDeck) {
                CreateNewDeckDialog(
                    confirmCreateNewDeckClicked = {
                        coroutineScope.launch {
                            viewModel.createNewDeck(
                                Deck(
                                    deckId = newDeckId,
                                    name = deckName,
                                    description = deckDescription,
                                    numOfCards = 0
                                )
                            )
                            delay(1000L) // Add delay to prevent navigating into
                        }                         // the new deck before it is created.
                        newDeckCreateClicked(newDeckId)
                    },
                    dismissCreateNewDeckClicked = { createNewDeck = false }
                )
            }
            if (cancelCreateNewDeck) {
                CancelCreateNewDeckDialog(
                    confirmCancelCreateNewDeckClicked = cancelCreateClicked,
                    dismissCancelCreateNewDeckClicked = { cancelCreateNewDeck = false }
                )
            }
        }
    }
}

@Composable
fun CreateNewDeckDialog(
    confirmCreateNewDeckClicked: () -> Unit,
    dismissCreateNewDeckClicked: () -> Unit
) {
    Dialog(onDismissRequest = dismissCreateNewDeckClicked) {
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
                    text = stringResource(id = R.string.create_new_deck_confirmation),
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
                    ElevatedButton(onClick = dismissCreateNewDeckClicked) {
                        Text(text = stringResource(R.string.button_back))
                    }
                    ElevatedButton(onClick = confirmCreateNewDeckClicked) {
                        Text(text = stringResource(R.string.button_yes))
                    }
                }
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
                    text = stringResource(id = R.string.quit_creating_new_deck_confirmation),
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
                        Text(text = stringResource(id = R.string.button_back))
                    }
                    ElevatedButton(onClick = confirmCancelCreateNewDeckClicked) {
                        Text(text = stringResource(id = R.string.button_yes))
                    }
                }
            }
        }
    }
}