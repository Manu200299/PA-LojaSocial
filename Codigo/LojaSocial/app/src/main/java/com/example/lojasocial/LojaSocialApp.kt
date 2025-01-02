package com.example.lojasocial

import android.app.Application
import com.google.firebase.FirebaseApp

class LojaSocialApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}

