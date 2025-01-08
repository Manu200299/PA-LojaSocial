package com.example.lojasocial.presentation.beneficiary

import android.app.appsearch.SearchResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.data.repository.VisitRepositoryImpl
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.model.Visit
import com.example.lojasocial.domain.use_case.AddBeneficiaryUseCase
import com.example.lojasocial.domain.use_case.BeneficiarySearchByIdNumberUseCase
import com.example.lojasocial.domain.use_case.BeneficiarySearchByPhoneNumberUseCase
import com.example.lojasocial.domain.use_case.CreateVisitUseCase
import com.example.lojasocial.domain.use_case.GetActiveVisitsForBeneficiaryUseCase
import com.example.lojasocial.domain.use_case.GetBeneficiariesUseCase
import com.example.lojasocial.domain.use_case.GetBeneficiaryByIdUseCase
import com.example.lojasocial.presentation.visit.VisitViewModel.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshot.StringNode
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

    // Repository for visit so it can start cvisit
    private val visitRepository = VisitRepositoryImpl(api)

    // Use cases for BENEFICIARY
    private val getBeneficiariesUseCase = GetBeneficiariesUseCase(repository)
    private val addBeneficiaryUseCase = AddBeneficiaryUseCase(repository)
    private val searchByPhoneNumberUseCase = BeneficiarySearchByPhoneNumberUseCase(repository)
    private val searchByIdNumberUseCase = BeneficiarySearchByIdNumberUseCase(repository)
    private val getBeneficiaryByIdUseCase = GetBeneficiaryByIdUseCase(repository)

    // Use cases for VISIT
    private val createVisitUseCase = CreateVisitUseCase(visitRepository)
    private val getActiveVisitsForBeneficiaryUseCase = GetActiveVisitsForBeneficiaryUseCase(visitRepository)

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Beneficiary>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow(SearchType.PHONE)
    val searchType = _searchType.asStateFlow()

    private val _selectedBeneficary = MutableStateFlow<Beneficiary?>(null)
    val selectedBeneficiary = _selectedBeneficary.asStateFlow()

    // currentVisit
    private val _currentVisit = MutableStateFlow<Visit?>(null)
    val currentVisit: StateFlow<Visit?> = _currentVisit.asStateFlow()

    private val _hasActiveVisit = MutableStateFlow(false)
    val hasActiveVisit: StateFlow<Boolean> = _hasActiveVisit.asStateFlow()

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

    // falta implementar no screen
    fun getBeneficiarybyId(beneficiaryId: String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try{
                val beneficiary = getBeneficiaryByIdUseCase(beneficiaryId)
                if (beneficiary != null) {
                    _selectedBeneficary.value = beneficiary
                    _uiState.value = UiState.Success
                } else {
                    _uiState.value = UiState.Error("Beneficiary not found!")
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.message ?: "Unkown Error")
                Log.e("BeneficiaryViewModel", "Error loading beneficiary: ${e.message}")
            }
        }
    }

    // Funcao de checkin
    fun startNewVisit(beneficiaryId: String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val activeVisit = getActiveVisitsForBeneficiaryUseCase(beneficiaryId)
                if (activeVisit != null) {
                    _currentVisit.value = activeVisit
                    _uiState.value = UiState.Success
                    Log.d("VisitViewModel", "Active visit found! Resuming active visit: $activeVisit")
                } else {
                    val newVisit = Visit(beneficiaryId = beneficiaryId)
                    val result = createVisitUseCase(newVisit)
                    result.onSuccess { visit ->
                        _currentVisit.value = visit
                        _uiState.value = UiState.Success
                        Log.d("VisitViewModel", "New visit started: $visit")
                    }.onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Failed to start visit")
                        Log.e("VisitViewModel", "Error starting new visit: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to start visit")
                Log.e("VisitViewModel", "Error starting new visit: ${e.message}")
            }
        }
    }

    // Funcao para verificar se ha uma visita ativa para esse beneficiario
    fun checkActiveVisit(beneficiaryId: String) {
        viewModelScope.launch {
            try {
                val activeVisit = getActiveVisitsForBeneficiaryUseCase(beneficiaryId)
                _hasActiveVisit.value = activeVisit != null
            } catch (e: Exception) {
                Log.e("BeneficiaryViewModel", "Error checking active visit: ${e.message}")
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