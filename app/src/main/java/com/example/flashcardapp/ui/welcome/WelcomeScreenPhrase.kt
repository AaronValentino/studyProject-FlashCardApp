package com.example.flashcardapp.ui.welcome

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcardapp.R
import com.example.flashcardapp.ui.welcome.WelcomeScreenPhrase.listOfWelcomePhraseId
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val welcomePhraseUiState = _welcomePhraseUiState.asStateFlow()

    private var coroutineStarted = false

    private var RunId = 0

    init {
        _welcomePhraseUiState.value = listOfWelcomePhraseId[listOfWelcomePhraseId.indices.random()]
        startLoop()
    }

    private var job: Job? = null

    private fun repeatInfinite(runCount: Int, runId: Int) {
        var runCountLocal = runCount
        Log.d("viewModelScope$runId", "Run started")
        job = viewModelScope.launch {
            while (true) {
                delay(3141L)
                _welcomePhraseUiState.value = listOfWelcomePhraseId[listOfWelcomePhraseId.indices.random()]
                Log.d("viewModelScope$runId", "Running $runCountLocal")
                runCountLocal++
            }
        }
    }

    fun startLoop() {
        if (!coroutineStarted) {
            RunId++
            coroutineStarted = true
            repeatInfinite(1, RunId)
        }
    }

    fun cancelJob() {
        job?.cancel()
        coroutineStarted = false
        Log.d("viewModelScope $RunId", "Cancelled")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WelcomeScreenViewModel()
            }
        }
    }
}