package com.example.lojasocial.presentation.volunteers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.domain.use_case.LoginVolunteerUseCase
import com.example.lojasocial.domain.use_case.RegisterVolunteerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VolunteerViewModel(
    private val registerUseCase: RegisterVolunteerUseCase,
    private val loginUseCase: LoginVolunteerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    /**
     * Registra um voluntário com os dados fornecidos.
     */
    fun registerVolunteer(
        email: String,
        senha: String,
        confirmarSenha: String
    ) {
        if (senha != confirmarSenha) {
            _uiState.value = UiState.Error("As senhas não coincidem.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = registerUseCase(email, senha)
            _uiState.value = if (result.isSuccess) UiState.Success else UiState.Error(
                result.exceptionOrNull()?.message ?: "Erro desconhecido."
            )
        }
    }

    /**
     * Realiza login de um voluntário com as credenciais fornecidas.
     */
    fun loginVolunteer(email: String, senha: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = loginUseCase(email, senha)
            _uiState.value = if (result.isSuccess) UiState.Success else UiState.Error(
                result.exceptionOrNull()?.message ?: "Erro desconhecido."
            )
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    class Factory(
        private val registerUseCase: RegisterVolunteerUseCase,
        private val loginUseCase: LoginVolunteerUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VolunteerViewModel::class.java)) {
                return VolunteerViewModel(registerUseCase, loginUseCase) as T
            }
            throw IllegalArgumentException("Classe de ViewModel desconhecida.")
        }
    }
}
