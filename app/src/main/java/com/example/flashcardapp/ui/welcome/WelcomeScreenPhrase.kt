package com.example.flashcardapp.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.R
import com.example.flashcardapp.ui.welcome.WelcomeScreenPhrase.listOfWelcomePhraseId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object WelcomeScreenPhrase {
    val listOfWelcomePhraseId = listOf(
        R.string.home_screen_welcome_phrase_arabic,
        R.string.home_screen_welcome_phrase_chinese_simplified,
        R.string.home_screen_welcome_phrase_dutch,
        R.string.home_screen_welcome_phrase_english,
        R.string.home_screen_welcome_phrase_french,
        R.string.home_screen_welcome_phrase_german,
        R.string.home_screen_welcome_phrase_greek,
        R.string.home_screen_welcome_phrase_hebrew,
        R.string.home_screen_welcome_phrase_hindi,
        R.string.home_screen_welcome_phrase_italian,
        R.string.home_screen_welcome_phrase_japanese,
        R.string.home_screen_welcome_phrase_korean,
        R.string.home_screen_welcome_phrase_portuguese,
        R.string.home_screen_welcome_phrase_russian,
        R.string.home_screen_welcome_phrase_spanish,
        R.string.home_screen_welcome_phrase_swedish,
        R.string.home_screen_welcome_phrase_turkish
    )
}

class WelcomeScreenViewModel : ViewModel() {
    private val _welcomePhraseUiState = MutableStateFlow(0)
    val welcomePhraseUiState: StateFlow<Int> = _welcomePhraseUiState.asStateFlow()

    private val _logoUiState = MutableStateFlow(true)
    val logoUiState: StateFlow<Boolean> = _logoUiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000L)
            _logoUiState.update { false }
        }
        _welcomePhraseUiState.value = listOfWelcomePhraseId[listOfWelcomePhraseId.indices.random()]
        startLoop()
    }

    private fun repeatInfinite() {
        viewModelScope.launch {
            while (true) {
                delay(2000L)
                _welcomePhraseUiState.value = listOfWelcomePhraseId[listOfWelcomePhraseId.indices.random()]
            }
        }
    }

    private fun startLoop() {
        repeatInfinite()
    }
}