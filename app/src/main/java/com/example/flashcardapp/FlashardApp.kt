package com.example.flashcardapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flashcardapp.navigation.FlashardNavHost

@Composable
fun FlashardApp(navController: NavHostController = rememberNavController()) {
    FlashardNavHost(navController = navController)
}