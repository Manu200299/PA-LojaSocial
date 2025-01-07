package com.example.lojasocial.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
        private val VOLUNTEER_ID_KEY = stringPreferencesKey("volunteer_id")
        private val VOLUNTEER_PHONE_KEY = stringPreferencesKey("volunteer_phone")
        private val VOLUNTEER_NAME_KEY = stringPreferencesKey("volunteer_name")
    }

    val volunteerId: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[VOLUNTEER_ID_KEY]
        }

    val telefone: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[VOLUNTEER_PHONE_KEY]
        }

    val nome: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[VOLUNTEER_NAME_KEY]
        }

    val isLoggedIn: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[VOLUNTEER_ID_KEY] != null
        }

    suspend fun saveVolunteerSession(id: String, phone: String, name: String) {
        context.dataStore.edit { preferences ->
            preferences[VOLUNTEER_ID_KEY] = id
            preferences[VOLUNTEER_PHONE_KEY] = phone
            preferences[VOLUNTEER_NAME_KEY] = name
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(VOLUNTEER_ID_KEY)
            preferences.remove(VOLUNTEER_PHONE_KEY)
            preferences.remove(VOLUNTEER_NAME_KEY)
        }
    }
}