package com.example.lojasocial.presentation.volunteers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.VolunteerRepositoryImpl
import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.domain.model.VolunteerLogin
import com.example.lojasocial.domain.use_case.VolunteerLoginUseCase
import com.example.lojasocial.domain.use_case.VolunteerRegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VolunteerViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private val api = FirebaseApi(auth, database)
    private val repository = VolunteerRepositoryImpl(api, sessionManager)

    // Use case para VOLUNTEER
    private val registerUseCase = VolunteerRegisterUseCase(repository)
    private val loginUseCase = VolunteerLoginUseCase(repository)

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun registerVolunteer(addVolunteer: Volunteer) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = registerUseCase(addVolunteer)
                result.fold(
                    onSuccess = { newVolunteer ->
                        _uiState.value = UiState.Success
                        Log.d("VolunteerRegistrationViewModel", "Registered volunteer: $newVolunteer")
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.message ?: "Unkown error")
                        Log.e("VolunteerRegistrationViewModel", "Error registering volunteer! | ${error.message}")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                Log.e("VolunteerRegistrationViewModel", "Error registering volunteer! | ${e.message}")
            }
        }
    }

    fun loginVolunteer(loginVolunteer: VolunteerLogin){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try{
                loginUseCase(loginVolunteer)
                _uiState.value = UiState.Success
                Log.d("VolunteerLoginViewModel", "Logging in volunteer: $loginVolunteer")
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.message ?: "Unkown error")
                Log.e("VolunteerLoginViewModel", "Error logging in volunteer! | ${e.message}")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VolunteerViewModel::class.java)) {
                return VolunteerViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unkown ViewModel class.")
        }
    }
}
