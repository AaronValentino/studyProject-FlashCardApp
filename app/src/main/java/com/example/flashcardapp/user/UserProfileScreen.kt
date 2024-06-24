package com.example.flashcardapp.user

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardapp.R

@Composable
fun UserProfileScreen(
    topBar: @Composable () -> Unit,
    backgroundBrush: Brush
) {
    val context = LocalContext.current
    var nameInEditMode by rememberSaveable {
        mutableStateOf(false)
    }
    var userName = stringResource(id = R.string.user_name)
    var userNameToChange by rememberSaveable {
        mutableStateOf(userName)
    }

    Scaffold(
        topBar = topBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = backgroundBrush)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(
                        horizontal = 52.dp,
                        vertical = 20.dp
                    )
                    .aspectRatio(1f),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 20.dp
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_foreground),
                    contentDescription = stringResource(id = R.string.user_profile_picture),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .aspectRatio(4.5f),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 20.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (nameInEditMode) {
                            OutlinedTextField(
                                value = userNameToChange,
                                onValueChange = {
                                    userNameToChange = it
                                    if (userNameToChange.length > 15) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.warning_username_too_long),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                isError = userNameToChange.length > 15,
                                label = {
                                    Text(
                                        text = stringResource(R.string.text_field_label, "<"),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clickable {
                                                if (userNameToChange.length > 15) {
                                                    Toast.makeText(
                                                            context,
                                                            context.getString(R.string.warning_invalid_username),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                } else {
                                                    userName = userNameToChange
                                                    nameInEditMode = false
                                                    Toast.makeText(
                                                            context,
                                                            context.getString(R.string.success_username_changed),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                }
                                            }
                                            .sizeIn(minHeight = 32.dp)
                                            .aspectRatio(1f)
                                            .padding(bottom = 8.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp)
                            )

                        } else {
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        nameInEditMode = true
                                    }
                                    .sizeIn(minHeight = 32.dp)
                                    .aspectRatio(1f)
                                    .padding(vertical = 12.dp)
                            )
                        }
                    }
                }
                ElevatedCard(
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .aspectRatio(1.5f),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 20.dp
                    )
                ) {
                    val motivationCard = randomMotivation((1..20).random())

                    motivationCard.let {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(id = it.phrase),
                                lineHeight = 28.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(
                                    id = R.string.motivation_author_format,
                                    stringResource(id = it.author)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.End,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}