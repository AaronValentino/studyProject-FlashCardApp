package com.example.flashcardapp.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardapp.MainActivity
import com.example.flashcardapp.R
import com.example.flashcardapp.ui.theme.FlashCardAppTheme

class WelcomeScreenActivity : ComponentActivity() {
    private val viewModel: WelcomeScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardAppTheme {
                WelcomeScreen(
                    onClicked = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}

@Composable
private fun WelcomeScreen(
    onClicked: () -> Unit,
    viewModel: WelcomeScreenViewModel
) {
    val welcomePhraseUiState = viewModel.welcomePhraseUiState.collectAsState()

    // Background Brush
    val backgroundBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.primary
        )
    )

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .clickable { onClicked() }
            .background(brush = backgroundBrush)
            .navigationBarsPadding()
    ) {
        AnimatedContent(
            targetState = welcomePhraseUiState.value,
            modifier = Modifier
                .background(color = Color.Transparent)
                .padding(horizontal = 20.dp),
            transitionSpec = {
                fadeIn(animationSpec = tween(1500, easing = LinearEasing))
                    .togetherWith(
                        fadeOut(animationSpec = tween(500, easing = FastOutSlowInEasing))
                    )
            },
            label = ""
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(horizontal = 16.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_screen_press_to_continue),
                    modifier = Modifier
                        .padding(8.dp),
                    fontSize = 12.sp
                )
            }
        }
    }
}