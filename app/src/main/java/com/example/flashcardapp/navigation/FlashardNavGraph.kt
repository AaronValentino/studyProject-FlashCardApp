package com.example.flashcardapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.flashcardapp.R
import com.example.flashcardapp.card.AddNewCardScreen
import com.example.flashcardapp.card.AllCardsScreen
import com.example.flashcardapp.deck.AddNewDeckScreen
import com.example.flashcardapp.deck.DeckListScreen
import com.example.flashcardapp.deck.SelectedDeckPageScreen
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
                onButtonClicked = { navController.navigate(DeckList) },
                topBar = {
                    TopAppBarWithIcon(
                        titleText = stringResource(id = R.string.app_name),
                        iconImageVector = Icons.Filled.Person,
                        iconContentDescription = stringResource(id = R.string.top_app_bar_user_profile_button),
                        onIconButtonClicked = { navController.navigate(UserProfile) }
                    )
                },
                backgroundBrush = backgroundBrush
            )
        }
        composable<DeckList> {
            DeckListScreen(
                topBar = {
                    TopAppBarNoIcon(
                        titleText = "Deck List",
                        showBackButton = true,
                        onBackButtonClicked = { navController.navigateUp() }
                    )
                },
                backgroundBrush = backgroundBrush,
                cardClicked = { deckId -> navController.navigate(SelectedDeckPage(deckId)) },
                addNewDeckClicked = { newDeckId -> navController.navigate(AddNewDeck(newDeckId)) }
            )
        }
        composable<AddNewDeck> {
            val addNewDeck: AddNewDeck = it.toRoute()
            AddNewDeckScreen(
                topBar = { TopAppBarNoIcon(titleText = "New Deck") },
                backgroundBrush = backgroundBrush,
                newDeckCreateClicked = { deckId ->
                    navController.popBackStack()
                    navController.navigate(SelectedDeckPage(deckId))
                },
                newDeckId = addNewDeck.newDeckId,
                cancelCreateClicked = { navController.navigateUp() }
            )
        }
        composable<SelectedDeckPage> {
            SelectedDeckPageScreen(
                backgroundBrush = backgroundBrush,
                onClickedBack = { navController.navigateUp() },
                onClickedConfirmDeleteDeck = { navController.navigateUp() },
                onClickedAddNewCard = { deckId, deckName -> navController.navigate(AddNewCard(deckId, deckName)) },
                onClickedAllCards = { deckId, deckName -> navController.navigate(AllCards(deckId, deckName)) },
                onClickedLesson = {}
            )
        }
        composable<AddNewCard> {
            val addNewCard: AddNewCard = it.toRoute()
            BackHandler {
                navController.navigateUp()
                navController.navigateUp()
                navController.navigate(SelectedDeckPage(addNewCard.deckId))
            }
            AddNewCardScreen(
                topBar = { TopAppBarNoIcon(titleText = addNewCard.deckName) },
                backgroundBrush = backgroundBrush,
                cancelCreateClicked = {
                    navController.navigateUp()
                    navController.navigateUp()
                    navController.navigate(SelectedDeckPage(addNewCard.deckId))
                }
            )
        }
        composable<AllCards> {
            val allCards: AllCards = it.toRoute()
            AllCardsScreen(
                topBar = {
                    TopAppBarNoIcon(
                        titleText = allCards.deckName,
                        showBackButton = true,
                        onBackButtonClicked = { navController.navigateUp() }
                    )
                },
                backgroundBrush = backgroundBrush,
                onClickedGoToLessons = {  }
            )
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
                        iconPadding = 2,
                        onIconButtonClicked = { navController.navigate(Home) }
                    )
                },
                backgroundBrush = backgroundBrush
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// For navigating between Home and User Profile
fun TopAppBarWithIcon(
    titleText: String,
    iconImageVector: ImageVector,
    iconContentDescription: String,
    iconPadding: Int = 0,
    onIconButtonClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = titleText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp,
                            end = 40.dp,
                            start = 40.dp
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
                            .padding(top = (4 + iconPadding).dp)
                            .clickable { onIconButtonClicked() }
                            .sizeIn(minWidth = 50.dp, minHeight = 55.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// No Icon but with back button
fun TopAppBarNoIcon(
    titleText: String,
    showBackButton: Boolean = false,
    onBackButtonClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = titleText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp,
                            end = 12.dp,
                            start = if (titleText.length > 9) 60.dp else 12.dp
                        ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (showBackButton) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { onBackButtonClicked() }
                            .sizeIn(minWidth = 50.dp, minHeight = 55.dp)
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