package com.example.lojasocial.presentation.visit

import androidx.annotation.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.StockRepositoryImpl
import com.example.lojasocial.data.repository.VisitRepositoryImpl
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.model.StockItem
import com.example.lojasocial.domain.model.Visit
import com.example.lojasocial.domain.model.VisitItem
import com.example.lojasocial.domain.use_case.CreateVisitUseCase
import com.example.lojasocial.domain.use_case.GetVisitsByBeneficiaryId
import com.example.lojasocial.domain.use_case.UpdateVisitUseCase
import com.example.lojasocial.presentation.beneficiary.BeneficiaryViewModel.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class VisitViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private val api = FirebaseApi(auth, database)
    private val repository = VisitRepositoryImpl(api)

    // Repository for stock so it can interact with items
    private val stockRepository = StockRepositoryImpl(api)

    // Use cases for VISIT
    private val createVisitUseCase = CreateVisitUseCase(repository)
    private val updateVisitUseCase = UpdateVisitUseCase(repository)
    private val getVisitsByBeneficiaryId = GetVisitsByBeneficiaryId(repository)

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _currentVisit = MutableStateFlow<Visit?>(null)
    val currentVisit: StateFlow<Visit?> = _currentVisit.asStateFlow()

    private val _stockItems = MutableStateFlow<List<StockItem>>(emptyList())
    val stockItems: StateFlow<List<StockItem>> = _stockItems.asStateFlow()

    fun loadStockItems(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            stockRepository.getStockItems().collect { items ->
                _stockItems.value = items
                _uiState.value = UiState.Success
            }
        }
    }

    // Funcao de checkin
    fun startNewVisit(beneficiaryId: String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val newList = Visit(beneficiaryId = beneficiaryId)
            val result = repository.createVisit(newList)
            result.onSuccess { visit ->
                _currentVisit.value = visit
                _uiState.value = UiState.Success
            }.onFailure { error ->
                _uiState.value = UiState.Error(error.message ?: "Failed to start visit")
            }
        }
    }

    fun addItemToVisit(stockItemId: String, quantity: Int) {
        val currentVisit = _currentVisit.value ?: return
        val updatedItems = currentVisit.items.toMutableList()
        val existingItem = updatedItems.find { it.stockItemId == stockItemId }
        if (existingItem != null) {
            updatedItems.remove(existingItem)
            updatedItems.add(existingItem.copy(quantity = existingItem.quantity + quantity))
        } else {
            updatedItems.add(VisitItem(stockItemId, quantity))
        }
        _currentVisit.value = currentVisit.copy(items = updatedItems)
    }

    fun removeItemFromVisit(stockItemId: String) {
        val currentVisit = _currentVisit.value ?: return
        val updatedItems = currentVisit.items.filter { it.stockItemId != stockItemId }
        _currentVisit.value = currentVisit.copy(items = updatedItems)
    }

    // Funcao de checkout
    fun finalizeVisit() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val currentVisit = _currentVisit.value ?: return@launch
            val result = repository.updateVisit(currentVisit.copy(endTime = Date()))
            result.onSuccess {
                _uiState.value = UiState.Success
                _currentVisit.value = null
            }.onFailure { error ->
                _uiState.value = UiState.Error(error.message ?: "Failed to finalize visit")
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
            if (modelClass.isAssignableFrom(VisitViewModel::class.java)) {
                return VisitViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unkown ViewModel class.")
        }
    }
}