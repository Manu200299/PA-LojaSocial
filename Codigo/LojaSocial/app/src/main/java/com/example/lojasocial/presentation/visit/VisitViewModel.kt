package com.example.lojasocial.presentation.visit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.StockRepositoryImpl
import com.example.lojasocial.data.repository.VisitRepositoryImpl
import com.example.lojasocial.domain.model.StockItem
import com.example.lojasocial.domain.model.Visit
import com.example.lojasocial.domain.model.VisitItem
import com.example.lojasocial.domain.use_case.CreateVisitUseCase
import com.example.lojasocial.domain.use_case.FinalizeVisitUseCase
import com.example.lojasocial.domain.use_case.GetActiveVisitsForBeneficiaryUseCase
import com.example.lojasocial.domain.use_case.GetStockCategoriesUseCase
import com.example.lojasocial.domain.use_case.GetStockItemsUseCase
import com.example.lojasocial.domain.use_case.GetVisitByIdUseCase
import com.example.lojasocial.domain.use_case.GetVisitsByBeneficiaryIdUseCase
import com.example.lojasocial.domain.use_case.UpdateVisitUseCase
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
    private val getVisitsByBeneficiaryIdUseCase = GetVisitsByBeneficiaryIdUseCase(repository)
    private val getActiveVisitsForBeneficiaryUseCase = GetActiveVisitsForBeneficiaryUseCase(repository)
    private val finalizeVisitUseCase = FinalizeVisitUseCase(repository)

    // Use cases for STOCK
    private val getStockItemUseCase = GetStockItemsUseCase(stockRepository)
    private val getStockCategoriesUseCase = GetStockCategoriesUseCase(stockRepository)


    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _currentVisit = MutableStateFlow<Visit?>(null)
    val currentVisit: StateFlow<Visit?> = _currentVisit.asStateFlow()

    private val _stockItems = MutableStateFlow<List<StockItem>>(emptyList())
    val stockItems: StateFlow<List<StockItem>> = _stockItems.asStateFlow()

    private val _filteredStockItems = MutableStateFlow<List<StockItem>>(emptyList())
    val filteredStockItems: StateFlow<List<StockItem>> = _filteredStockItems.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private var searchQuery: String = ""
//    private var selectedCategory: String? = null

    // Function para dar load aos items em stock sempre que o viewmodel e chamado
    init {
        loadStockItems()
        loadCategories()
    }

    fun loadVisit(beneficiaryId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val activeVisit = getActiveVisitsForBeneficiaryUseCase(beneficiaryId)
                if (activeVisit != null) {
                    _currentVisit.value = activeVisit
                    _uiState.value = UiState.Success
                } else {
                    _uiState.value = UiState.Error("No active visit found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load visit")
            }
        }
    }

    fun loadStockItems(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try{
                getStockItemUseCase().collect { items ->
                    _stockItems.value = items
                    applyFilters()
                    _uiState.value = UiState.Success
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.message ?: "Failed to load stock items")
                Log.e("VisitViewModel", "Error loading stock items: ${e.message}")
            }
        }
    }

    private fun loadCategories(){
        viewModelScope.launch {
            try{
                getStockCategoriesUseCase().collect { fetchedCategories ->
                    _categories.value = fetchedCategories
                }
            } catch (e: Exception){
                Log.e("VisitViewModel", "Error loading categories: ${e.message}")
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
            _uiState.value = UiState.Loading
            try{
                val activeVisit = getActiveVisitsForBeneficiaryUseCase(beneficiaryId)
                if (activeVisit != null) {
                    _currentVisit.value = activeVisit
                    _uiState.value = UiState.Success
                    Log.d("VisitViewModel", "Active visit found: $activeVisit")
                } else {
                    startNewVisit(beneficiaryId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to check active visit")
                Log.e("VisitViewModel", "Error checking active visit: ${e.message}")
            }
        }
    }

    // Adiciona e remove tambem
    fun addItemToVisit(stockItemId: String, quantity: Int) {
        viewModelScope.launch {
            val currentVisit = _currentVisit.value ?: return@launch
            val updatedItems = currentVisit.items.toMutableList()
            val existingItem = updatedItems.find { it.stockItemId == stockItemId }
            val stockItem = _stockItems.value.find { it.itemId == stockItemId } ?: return@launch

            val currentQuantity = existingItem?.quantity ?: 0
            val newQuantity = (currentQuantity + quantity).coerceIn(0, stockItem.quantidade)

            if (newQuantity > 0) {
                if (existingItem != null) {
                    updatedItems[updatedItems.indexOf(existingItem)] = existingItem.copy(quantity = newQuantity)
                } else {
                    updatedItems.add(VisitItem(stockItemId, newQuantity))
                }
            } else {
                updatedItems.remove(existingItem)
            }

            val updatedVisit = currentVisit.copy(items = updatedItems)
            _currentVisit.value = updatedVisit

            updateVisitUseCase(updatedVisit)
        }
    }

    fun removeItemFromVisit(stockItemId: String) {
        addItemToVisit(stockItemId, -1)
    }

    // Funcao de checkout
    fun finalizeVisit() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val currentVisit = _currentVisit.value ?: throw IllegalStateException("No active visit")
                val result = finalizeVisitUseCase(currentVisit.id)
//                val result = updateVisitUseCase(currentVisit.copy(endTime = Date()))
                result.onSuccess {
                    _uiState.value = UiState.Success
                    _currentVisit.value = null
                    Log.d("VisitViewModel", "Visit finalized successfully")
                }.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to finalize visit")
                    Log.e("VisitViewModel", "Error finalizing visit: ${error.message}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to finalize visit")
                Log.e("VisitViewModel", "Error finalizing visit: ${e.message}")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        applyFilters()
    }

    fun updateSelectedCategory(category: String?) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun performSearch(){
        applyFilters()
    }

    private fun applyFilters() {
        _filteredStockItems.value = _stockItems.value.filter { item ->
            (searchQuery.isEmpty() || item.nome.contains(searchQuery, ignoreCase = true)) &&
                    (_selectedCategory.value == null || item.categoria == _selectedCategory.value)
        }.sortedBy { it.nome }
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