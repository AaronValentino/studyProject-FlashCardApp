package com.example.flashcardapp.ui

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flashcardapp.R
import com.example.flashcardapp.navigation.FlashardNavHost
import com.example.flashcardapp.ui.welcome.WelcomeScreenViewModel

@Composable
fun FlashardApp(navController: NavHostController = rememberNavController()) {
    var screen by remember {
        mutableIntStateOf(0)
    }

    when (screen) {
        1 -> FlashardNavHost(navController = navController)
        else -> WelcomeScreen(onClicked = { screen = 1 })
    }
}

@Composable
private fun WelcomeScreen(
    onClicked: () -> Unit,
    viewModel: WelcomeScreenViewModel = viewModel(factory = WelcomeScreenViewModel.Factory)
) {
    val welcomePhraseUiState = viewModel.welcomePhraseUiState.collectAsState()

    // Background Brush
    val backgroundBrushDown = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background
        )
    )
    val backgroundBrushUp = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.primary
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                viewModel.cancelJob()
                onClicked()
            }
            .systemBarsPadding()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(brush = backgroundBrushDown)
        )
        AnimatedContent(
            targetState = welcomePhraseUiState.value,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
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
                    .fillMaxWidth(),
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
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(brush = backgroundBrushUp),
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