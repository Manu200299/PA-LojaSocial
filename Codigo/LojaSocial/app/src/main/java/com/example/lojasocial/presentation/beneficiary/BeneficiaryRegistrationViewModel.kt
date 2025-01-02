package com.example.lojasocial.presentation.beneficiary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.model.Beneficiary
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class BeneficiaryRegistrationViewModel : ViewModel() {
    private val database = Firebase.database.reference
    private val TAG = "BeneficiaryRegistrationViewModel"

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()

    fun registerBeneficiary(nome: String) {
        viewModelScope.launch {
            try {
                _registrationState.value = RegistrationState.Loading
                Log.d(TAG, "Starting beneficiary registration")

                if (nome.isBlank()) {
                    Log.e(TAG, "Validation failed: Name is blank")
                    _registrationState.value = RegistrationState.Error("Por favor, preencha o nome do beneficiário")
                    return@launch
                }

                val beneficiary = Beneficiary(
                    id = UUID.randomUUID().toString(),
                    nome = nome
                )

                Log.d(TAG, "Attempting to save beneficiary: $beneficiary")
                database.child("Beneficiarios").child(beneficiary.id).setValue(beneficiary).await()
                Log.d(TAG, "Beneficiary saved successfully")
                _registrationState.value = RegistrationState.Success
            } catch (e: Exception) {
                Log.e(TAG, "Error registering beneficiary", e)
                _registrationState.value = RegistrationState.Error(e.message ?: "Erro ao registrar beneficiário")
            }
        }
    }

    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        object Success : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }
}
