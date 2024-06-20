package com.example.flashcardapp.navigation

import com.example.flashcardapp.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object Screen_Home : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.nav_home_title
}

object Screen_DeckList : NavigationDestination {
    override val route = "decks"
    override val titleRes = R.string.nav_deck_list_title
}

object Screen_UserProfile : NavigationDestination {
    override val route = "user_profile"
    override val titleRes = R.string.nav_user_profile_title
}