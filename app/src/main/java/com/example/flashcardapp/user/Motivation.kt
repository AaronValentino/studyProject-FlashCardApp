package com.example.flashcardapp.user

import androidx.annotation.StringRes
import com.example.flashcardapp.R

fun randomMotivation(randNum: Int): motivationCard {
    return when (randNum)
        {
            1 -> motivationCard(R.string.motivation_1_phrase, R.string.motivation_1_author)
            2 -> motivationCard(R.string.motivation_2_phrase, R.string.motivation_2_author)
            3 -> motivationCard(R.string.motivation_3_phrase, R.string.motivation_3_author)
            4 -> motivationCard(R.string.motivation_4_phrase, R.string.motivation_4_author)
            5 -> motivationCard(R.string.motivation_5_phrase, R.string.motivation_5_author)
            6 -> motivationCard(R.string.motivation_6_phrase, R.string.motivation_6_author)
            7 -> motivationCard(R.string.motivation_7_phrase, R.string.motivation_7_author)
            8 -> motivationCard(R.string.motivation_8_phrase, R.string.motivation_8_author)
            9 -> motivationCard(R.string.motivation_9_phrase, R.string.motivation_9_author)
            10 -> motivationCard(R.string.motivation_10_phrase, R.string.motivation_10_author)
            12 -> motivationCard(R.string.motivation_12_phrase, R.string.motivation_12_author)
            13 -> motivationCard(R.string.motivation_13_phrase, R.string.motivation_13_author)
            14 -> motivationCard(R.string.motivation_14_phrase, R.string.motivation_14_author)
            15 -> motivationCard(R.string.motivation_15_phrase, R.string.motivation_15_author)
            16 -> motivationCard(R.string.motivation_16_phrase, R.string.motivation_16_author)
            17 -> motivationCard(R.string.motivation_17_phrase, R.string.motivation_17_author)
            18 -> motivationCard(R.string.motivation_18_phrase, R.string.motivation_18_author)
            19 -> motivationCard(R.string.motivation_19_phrase, R.string.motivation_19_author)
            else -> motivationCard(R.string.motivation_20_phrase, R.string.motivation_20_author)
        }
}

data class motivationCard(
    @StringRes val phrase: Int,
    @StringRes val author: Int
)