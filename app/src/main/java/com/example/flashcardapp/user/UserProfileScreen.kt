package com.example.flashcardapp.user

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardapp.MainActivity
import com.example.flashcardapp.R

@Composable
fun UserProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(
                    horizontal = 52.dp,
                    vertical = 20.dp
                )
                .aspectRatio(1f)
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
                    .aspectRatio(5f),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.user_name)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        modifier = Modifier
                            .sizeIn(minHeight = 40.dp)
                            .aspectRatio(1f)
                    )
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .sizeIn(minHeight = 48.dp)
                    .aspectRatio(1.5f),
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