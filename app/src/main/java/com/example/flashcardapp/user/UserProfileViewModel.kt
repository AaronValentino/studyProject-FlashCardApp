package com.example.flashcardapp.user

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val _motivationPhraseUiState = MutableStateFlow("")
    val motivationPhraseUiState: StateFlow<String> = _motivationPhraseUiState.asStateFlow()
    private val _motivationAuthorUiState = MutableStateFlow("")
    val motivationAuthorUiState: StateFlow<String> = _motivationAuthorUiState.asStateFlow()

    fun setMotivationPhrase(context: Context) {
        Log.d("setMotivationPhrase", "ran")
        val motivationCard = randomMotivation((1..20).random())
        _motivationAuthorUiState.value = context.getString(motivationCard.author)
        val motivationPhrase = context.getString(motivationCard.phrase)

        viewModelScope.launch {
            for (x in motivationPhrase) {
                _motivationPhraseUiState.update {
                    it.plus(x)
                }
                delay(50L)
            }
        }
    }
}

fun randomMotivation(randNum: Int): MotivationCard {
    return when (randNum)
        {
            1 -> MotivationCard(R.string.motivation_1_phrase, R.string.motivation_1_author)
            2 -> MotivationCard(R.string.motivation_2_phrase, R.string.motivation_2_author)
            3 -> MotivationCard(R.string.motivation_3_phrase, R.string.motivation_3_author)
            4 -> MotivationCard(R.string.motivation_4_phrase, R.string.motivation_4_author)
            5 -> MotivationCard(R.string.motivation_5_phrase, R.string.motivation_5_author)
            6 -> MotivationCard(R.string.motivation_6_phrase, R.string.motivation_6_author)
            7 -> MotivationCard(R.string.motivation_7_phrase, R.string.motivation_7_author)
            8 -> MotivationCard(R.string.motivation_8_phrase, R.string.motivation_8_author)
            9 -> MotivationCard(R.string.motivation_9_phrase, R.string.motivation_9_author)
            10 -> MotivationCard(R.string.motivation_10_phrase, R.string.motivation_10_author)
            12 -> MotivationCard(R.string.motivation_12_phrase, R.string.motivation_12_author)
            13 -> MotivationCard(R.string.motivation_13_phrase, R.string.motivation_13_author)
            14 -> MotivationCard(R.string.motivation_14_phrase, R.string.motivation_14_author)
            15 -> MotivationCard(R.string.motivation_15_phrase, R.string.motivation_15_author)
            16 -> MotivationCard(R.string.motivation_16_phrase, R.string.motivation_16_author)
            17 -> MotivationCard(R.string.motivation_17_phrase, R.string.motivation_17_author)
            18 -> MotivationCard(R.string.motivation_18_phrase, R.string.motivation_18_author)
            19 -> MotivationCard(R.string.motivation_19_phrase, R.string.motivation_19_author)
            else -> MotivationCard(R.string.motivation_20_phrase, R.string.motivation_20_author)
        }
}

data class MotivationCard(
    @StringRes val phrase: Int,
    @StringRes val author: Int
)