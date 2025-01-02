package com.example.lojasocial

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.firebase.FirebaseApp

class LojaSocialApplication : Application() {
    lateinit var dependencies: AppDependencies

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        dependencies = AppDependencies()
    }
}