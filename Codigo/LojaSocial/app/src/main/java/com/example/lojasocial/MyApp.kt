package com.example.lojasocial

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializa o Firebase
        FirebaseApp.initializeApp(this)

        // Activa a persistência offline do Firebase Realtime Database
        val db = FirebaseDatabase.getInstance()
        db.setPersistenceEnabled(true)
    }
}