package com.example.lojasocial.presentation.statistics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import com.example.lojasocial.presentation.statistics.components.NationalityStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val beneficiaryRepository: BeneficiaryRepository
) : ViewModel() {
    // Nacionalidades (para o gráfico de barras)
    private val _nationalitiesData = MutableStateFlow<List<NationalityStats>>(emptyList())
    val nationalitiesData: StateFlow<List<NationalityStats>> = _nationalitiesData.asStateFlow()
    // Dados mensais simulados (para o gráfico de linha)
    private val _monthlyData = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val monthlyData: StateFlow<List<Pair<String, Int>>> = _monthlyData.asStateFlow()
    init {
        loadStatistics()
    }
    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                // Obter dados de beneficiários
                val beneficiaries = beneficiaryRepository.getBeneficiaries()
                beneficiaries.collect { list ->
                    // Nacionalidades (gráfico de barras)
                    _nationalitiesData.value = list
                        .groupBy { it.nacionalidade.ifBlank { "Desconhecido" } }
                        .map { (nacionalidade, groupedList) ->
                            NationalityStats(
                                country = nacionalidade,
                                count = groupedList.size
                            )
                        }
                    // Simular dados mensais (gráfico de linha)
                    _monthlyData.value = listOf(
                        "Jan" to 50,
                        "Feb" to 45,
                        "Mar" to 60,
                        "Apr" to 70,
                        "May" to 75,
                        "Jun" to 65
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace() // Tratar erros
            }
        }
    }
    // Factory para injetar o BeneficiaryRepository
    class Factory(
        private val beneficiaryRepository: BeneficiaryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
                return StatisticsViewModel(beneficiaryRepository) as T
            }
            throw IllegalArgumentException("ViewModel desconhecido.")
        }
    }
}