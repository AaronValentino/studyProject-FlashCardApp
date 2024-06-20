package com.example.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.flashcardapp.ui.theme.FlashCardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            FlashCardAppTheme {
                FlashardApp()
            }
        }
    }
}

//@Preview(
//    name = "Light",
//    showBackground = true,
//    showSystemUi = true,
//    uiMode = 0
//)
//@Composable
//fun PreviewApp() {
//    FlashCardAppTheme {
//        HomeScreen()
//    }
//}