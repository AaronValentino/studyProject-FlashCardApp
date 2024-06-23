package com.example.flashcardapp.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcardapp.R
import com.example.flashcardapp.ui.home.HomeScreen
import com.example.flashcardapp.user.UserProfileScreen

// Navigation Graph
@Composable
fun FlashardNavHost(
    navController: NavHostController
) {
    // Background Brush
    val backgroundBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background
        )
    )

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                onButtonClicked = {
                    navController.navigate(DeckList)
                },
                topBar = {
                    TopAppBarWithIcon(
                        titleText = stringResource(id = R.string.app_name),
                        iconImageVector = Icons.Filled.Person,
                        iconContentDescription = stringResource(id = R.string.top_app_bar_user_profile_button),
                        onButtonClicked = {
                            navController.navigate(UserProfile)
                        }
                    )
                },
                backgroundBrush = backgroundBrush
            )
        }
        composable<DeckList> {
        }
        composable<SelectedDeckPage> {
        }
        composable<CardList> {
        }
        composable<CardQuiz> {
        }
        composable<UserProfile> {
            UserProfileScreen(
                topBar = {
                    TopAppBarWithIcon(
                        titleText = stringResource(id = R.string.nav_user_profile_title),
                        iconImageVector = Icons.Filled.Home,
                        iconContentDescription = stringResource(id = R.string.top_app_bar_home_button),
                        onButtonClicked = {
                            navController.navigate(Home)
                        }
                    )
                },
                backgroundBrush = backgroundBrush
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithIcon(
    titleText: String,
    iconImageVector: ImageVector,
    iconContentDescription: String,
    onButtonClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = titleText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp
                        ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = iconImageVector,
                        contentDescription = iconContentDescription,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { onButtonClicked() }
                            .sizeIn(minWidth = 50.dp, minHeight = 55.dp),
                    )
                }
            }
        },
        windowInsets = WindowInsets(top = 40.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}