package com.example.lojasocial.presentation.beneficiary

import android.app.appsearch.SearchResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.use_case.AddBeneficiaryUseCase
import com.example.lojasocial.domain.use_case.BeneficiarySearchByIdNumberUseCase
import com.example.lojasocial.domain.use_case.BeneficiarySearchByPhoneNumberUseCase
import com.example.lojasocial.domain.use_case.GetBeneficiariesUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BeneficiaryViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private val api = FirebaseApi(auth, database)
    private val repository = BeneficiaryRepositoryImpl(api)

    // Use cases for BENEFICIARY
    private val getBeneficiariesUseCase = GetBeneficiariesUseCase(repository)
    private val addBeneficiaryUseCase = AddBeneficiaryUseCase(repository)
    private val searchByPhoneNumberUseCase = BeneficiarySearchByPhoneNumberUseCase(repository)
    private val searchByIdNumberUseCase = BeneficiarySearchByIdNumberUseCase(repository)

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Beneficiary>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow(SearchType.PHONE)
    val searchType = _searchType.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSearchTypeChange(type: SearchType) {
        _searchType.value = type
    }

    fun performSearch() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val results = when (_searchType.value) {
                    SearchType.PHONE -> searchByPhoneNumberUseCase(_searchQuery.value)
                    SearchType.ID -> searchByIdNumberUseCase(_searchQuery.value)
                }
                results.collect { beneficiaries ->
                    _searchResults.value = beneficiaries
                    _uiState.value = if (beneficiaries.isEmpty()) UiState.NotFound else UiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                Log.e("BeneficiaryViewModel", "Error searching beneficiaries: ${e.message}")
            }
        }
    }

    fun addBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = addBeneficiaryUseCase(beneficiary)
                result.fold(
                    onSuccess = {
                        _uiState.value = UiState.Success
                        Log.d("BeneficiaryViewModel", "Added beneficiary: $beneficiary")
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.message ?: "Unknown error")
                        Log.e("BeneficiaryViewModel", "Error adding beneficiary: ${error.message}")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                Log.e("BeneficiaryViewModel", "Error adding beneficiary: ${e.message}")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        object NotFound : UiState()
        data class Error(val message: String) : UiState()
    }

    enum class SearchType {
        PHONE, ID
    }

    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BeneficiaryViewModel::class.java)) {
                return BeneficiaryViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class.")
        }
    }
}