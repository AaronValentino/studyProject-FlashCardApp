package com.example.flashcardapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flashcardapp.MainActivity
import com.example.flashcardapp.navigation.FlashardNavHost

@Composable
fun FlashardApp(
    navController: NavHostController = rememberNavController(),
    mainActivity: MainActivity
) {
    FlashardNavHost(
        navController = navController,
        mainActivity = mainActivity)
}
