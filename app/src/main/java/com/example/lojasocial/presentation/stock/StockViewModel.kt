package com.example.lojasocial.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.StockRepositoryImpl
import com.example.lojasocial.domain.model.StockItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseApi = FirebaseApi(firebaseAuth, firebaseDatabase)

    private val repository = StockRepositoryImpl(firebaseApi)

    // Lista de items de stock observável pela UI
    private val _stockItemsState = MutableStateFlow<List<StockItem>>(emptyList())
    val stockItemsState: StateFlow<List<StockItem>> = _stockItemsState.asStateFlow()

    // Mensagem de erro (caso aconteça alguma falha nas operações)
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    init {
        loadStockItems()
    }

    /**
     * Carrega todos os itens de stock do Firebase e actualiza o estado.
     */
    private fun loadStockItems() {
        viewModelScope.launch {
            repository.getStockItems()
                .catch { e ->
                    _errorState.value = e.message
                }
                .collect { items ->
                    _stockItemsState.value = items
                }
        }
    }

    /**
     * Adiciona um novo item ao stock.
     */
    fun addNewItem(item: StockItem) {
        viewModelScope.launch {
            val result = repository.addStockItem(item)
            result.onFailure { e ->
                _errorState.value = e.message
            }
            // Caso queiras voltar a carregar a lista de stock, podes chamar loadStockItems() aqui
        }
    }

    /**
     * Actualiza um item existente (por exemplo, ao alterar quantidade).
     */
    fun updateItem(item: StockItem) {
        viewModelScope.launch {
            val result = repository.updateStockItem(item)
            result.onFailure { e ->
                _errorState.value = e.message
            }
        }
    }

    /**
     * Remove um item do stock, dado o seu itemId.
     */
    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            val result = repository.deleteStockItem(itemId)
            result.onFailure { e ->
                _errorState.value = e.message
            }
        }
    }

    // Caso precises de injecção de dependências, podes criar um Factory:
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
                return StockViewModel() as T
            }
            throw IllegalArgumentException("Classe de ViewModel desconhecida.")
        }
    }
}
