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

    fun setUiStateError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    /**
     * Valida os dados do voluntário antes do registro.
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Registra um voluntário com os dados fornecidos.
     */
    fun registerVolunteer(
        nome: String,
        email: String,
        telefone: String,
        password: String,
        confirmarPassword: String,
        dataNascimento: String
    ) {
        if (!isValidEmail(email)) {
            _uiState.value = UiState.Error("Email inválido.")
            return
        }
        if (!isValidPassword(password)) {
            _uiState.value = UiState.Error("A password deve ter pelo menos 6 caracteres.")
            return
        }
        if (password != confirmarPassword) {
            _uiState.value = UiState.Error("As passwords não coincidem.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = registerUseCase(nome, email, telefone, password, dataNascimento)
            _uiState.value = if (result.isSuccess) UiState.Success else UiState.Error(
                result.exceptionOrNull()?.message ?: "Erro desconhecido ao registrar."
            )
        }
    }

    /**
     * Realiza login de um voluntário com as credenciais fornecidas.
     */
    fun loginVolunteer(email: String, password: String) {
        if (!isValidEmail(email)) {
            _uiState.value = UiState.Error("Email inválido.")
            return
        }
        if (password.isBlank()) {
            _uiState.value = UiState.Error("A password não pode estar vazia.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = loginUseCase(email, password)
            _uiState.value = if (result.isSuccess) UiState.Success else UiState.Error(
                result.exceptionOrNull()?.message ?: "Erro desconhecido ao fazer login."
            )
        }
    }

    /**
     * Reseta o estado da UI para Idle.
     */
    fun resetUiState() {
        _uiState.value = UiState.Idle
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
