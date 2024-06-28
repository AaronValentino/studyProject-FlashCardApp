package com.example.flashcardapp.deck

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flashcardapp.R
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckConstant

@Composable
fun EditDeckDialog(
    confirmEditDeckDetailsClicked: (String, String) -> Unit,
    dismissEditDeckDetailsClicked: () -> Unit,
    confirmDeleteDeckClicked: () -> Unit,
    currentDeck: Deck,
) {
    val oldDeckName = currentDeck.name
    var editDeckName by rememberSaveable {
        mutableStateOf(oldDeckName)
    }
    val oldDeckDescription = currentDeck.description
    var editDeckDescription by rememberSaveable {
        mutableStateOf(oldDeckDescription)
    }
    var showConfirmDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showConfirmDeleteDialog) {
        ConfirmDeleteDeckDialog(
            confirmDeleteDeckClicked = confirmDeleteDeckClicked,
            dismissDeleteDeckClicked = { showConfirmDeleteDialog = false }
        )
    }

    Dialog(onDismissRequest = dismissEditDeckDetailsClicked) {
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
                            value = editDeckName,
                            onValueChange = { editDeckName = it },
                            label = {
                                if (editDeckName.length > DeckConstant.DECKNAMELENGTH) {
                                    Text(
                                        text = stringResource(id = R.string.textfield_label_edit_deck_name_too_long),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else if (editDeckName.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.textfield_label_edit_deck_name_is_empty),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.textfield_label_edit_deck_name),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            isError = editDeckName.length > DeckConstant.DECKNAMELENGTH
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
                                if (editDeckDescription.length > DeckConstant.DECKDESCRIPTIONLENGTH) {
                                    Text(
                                        text = stringResource(id = R.string.textfield_label_edit_deck_description_too_long),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.textfield_label_edit_deck_description),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            isError = editDeckDescription.length > DeckConstant.DECKDESCRIPTIONLENGTH
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.25f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ElevatedButton(onClick = dismissEditDeckDetailsClicked) {
                            Text(text = stringResource(id = R.string.button_cancel))
                        }
                        ElevatedButton(
                            onClick = {
                                confirmEditDeckDetailsClicked(editDeckName, editDeckDescription)
                            },
                            enabled = (
                                    editDeckName.length <= DeckConstant.DECKNAMELENGTH &&
                                            editDeckName.isNotEmpty() &&
                                            editDeckDescription.length <= DeckConstant.DECKDESCRIPTIONLENGTH &&
                                            ((editDeckName != oldDeckName) || (editDeckDescription != oldDeckDescription))
                                    )
                        ) {
                            Text(stringResource(id = R.string.button_done))
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
                    contentDescription = stringResource(id = R.string.contentDescription_button_delete_deck),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteDeckDialog(
    confirmDeleteDeckClicked: () -> Unit,
    dismissDeleteDeckClicked: () -> Unit
) {
    Dialog(onDismissRequest = dismissDeleteDeckClicked) {
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
                    text = stringResource(id = R.string.delete_deck_confirmation),
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
                    ElevatedButton(onClick = dismissDeleteDeckClicked) {
                        Text(text = stringResource(id = R.string.button_cancel))
                    }
                    ElevatedButton(onClick = confirmDeleteDeckClicked) {
                        Text(text = stringResource(id = R.string.button_yes))
                    }
                }
            }
        }
    }
}