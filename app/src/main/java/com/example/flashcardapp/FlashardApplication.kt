package com.example.flashcardapp

import android.app.Application
import com.example.flashcardapp.data.AppContainer
import com.example.flashcardapp.data.AppDataContainer

class FlashardApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}