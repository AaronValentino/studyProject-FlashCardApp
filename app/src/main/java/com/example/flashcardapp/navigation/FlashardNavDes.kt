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
    override val route = "deck_list"
    override val titleRes = R.string.nav_deck_list_title
}

object Screen_DeckPage : NavigationDestination {
    override val route = "deck_page"
    override val titleRes = R.string.nav_deck_page_title
}

object Screen_Card_StudyMode : NavigationDestination {
    override val route = "card_study_mode"
    override val titleRes = R.string.nav_card_study_mode_title
}

object Screen_CardList : NavigationDestination {
    override val route = "card_"
    override val titleRes = R.string.nav_card_list_title
}


object Screen_UserProfile : NavigationDestination {
    override val route = "user_profile"
    override val titleRes = R.string.nav_user_profile_title
}