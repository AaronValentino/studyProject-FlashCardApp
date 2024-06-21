package com.example.flashcardapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcardapp.R
import com.example.flashcardapp.card.CardListScreen
import com.example.flashcardapp.deck.Deck1
import com.example.flashcardapp.deck.DeckListScreen
import com.example.flashcardapp.ui.home.HomeScreen
import com.example.flashcardapp.user.UserProfileScreen

// Navigation Graph
@Composable
fun FlashardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Background Brush
    val backgroundBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background
        )
    )

    // TopAppBar Icon
    var topAppBarIcon by remember {
        mutableStateOf(Screen_Home.route)
    }

    // TopAppBar Route
    val topAppBarOnClickedUserProfile: () -> Unit = {
        navController.navigate(Screen_UserProfile.route)
    }
    val topAppBarOnClickedHome: () -> Unit = {
        navController.popBackStack(
            Screen_Home.route,
            inclusive = false
        )
    }

    val topAppBarOnClicked: () -> Unit = when(topAppBarIcon) {
        "home" -> topAppBarOnClickedUserProfile
        else -> topAppBarOnClickedHome
    }

    Scaffold(
        topBar = {
            TopAppBar(topAppBarIcon = topAppBarIcon) { topAppBarOnClicked() }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen_Home.route,
            modifier = modifier
                .padding(innerPadding)
                .background(brush = backgroundBrush)
        ) {
            composable(route = Screen_Home.route) {
                topAppBarIcon = Screen_Home.route
                HomeScreen( onButtonClicked = { navController.navigate(Screen_DeckList.route) } )
            }
            composable(route = Screen_DeckList.route) {
                topAppBarIcon = Screen_DeckList.route
                DeckListScreen()
            }
            composable(route = Screen_DeckPage.route) {
            }
            composable(route = Screen_CardList.route) {
                CardListScreen(
                    deckName = Deck1.deck.deckName,
                    deckCards = Deck1.deck.deckCards
                ) {topAppBarIcon = it}
            }
            composable(route = Screen_UserProfile.route) {
                topAppBarIcon = Screen_UserProfile.route
                UserProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    titleText: String = stringResource(id = R.string.app_name),
    topAppBarIcon: String,
    onButtonClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = titleText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp
                        ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (topAppBarIcon) {
                        "home" -> {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = stringResource(id = R.string.top_app_bar_user_profile_button),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .clickable { onButtonClicked() }
                                    .sizeIn(minWidth = 50.dp, minHeight = 55.dp),
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = stringResource(id = R.string.top_app_bar_home_button),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .clickable { onButtonClicked() }
                                    .sizeIn(minWidth = 50.dp, minHeight = 55.dp),
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}