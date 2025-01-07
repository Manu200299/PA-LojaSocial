//package com.example.lojasocial.presentation.beneficiary
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.lojasocial.data.remote.model.BeneficiaryDto
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//class CheckInBeneficiaryViewModel(
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(CheckInUiState())
//    val uiState = _uiState.asStateFlow()
//
////    fun onFamilySearchQueryChange(query: String) {
////        _uiState.update { it.copy(familySearchQuery = query) }
////    }
//
//    fun onNumberSearchQueryChange(query: String) {
//        _uiState.update { it.copy(numberSearchQuery = query) }
//    }
//
//    fun onSearchTypeChange(searchType: SearchType) {
//        _uiState.update {
//            it.copy(
//                selectedSearchType = searchType,
//                numberSearchQuery = "",
//                searchResult = null
//            )
//        }
//    }
//
//    fun performSearch() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSearching = true, searchResult = null) }
//            val results = when {
////                _uiState.value.familySearchQuery.isNotEmpty() ->
////                    searchBeneficiariesUseCase.searchByFamily(_uiState.value.familySearchQuery)
//                _uiState.value.selectedSearchType == SearchType.PHONE ->
//                    searchBeneficiariesUseCase.searchByPhoneNumber(_uiState.value.numberSearchQuery)
//                else ->
//                    searchBeneficiariesUseCase.searchByIdNumber(_uiState.value.numberSearchQuery)
//            }
//            results.collect { beneficiaries ->
//                _uiState.update {
//                    it.copy(
//                        isSearching = false,
//                        searchResult = if (beneficiaries.isNotEmpty()) SearchResult.Found(beneficiaries) else SearchResult.NotFound
//                    )
//                }
//            }
//        }
//    }
//
//    class Factory(
//        private val searchBeneficiariesUseCase: SearchBeneficiaryUseCase
//    ) : ViewModelProvider.Factory {
//        @Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(CheckInBeneficiaryViewModel::class.java)) {
//                return CheckInBeneficiaryViewModel(searchBeneficiariesUseCase) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//}
//
//data class CheckInUiState(
//    val familySearchQuery: String = "",
//    val numberSearchQuery: String = "",
//    val selectedSearchType: SearchType = SearchType.PHONE,
//    val isSearching: Boolean = false,
//    val searchResult: SearchResult? = null
//)
//
//enum class SearchType {
//    PHONE, ID
//}
//
//sealed class SearchResult {
//    data class Found(val beneficiaries: List<BeneficiaryDto>) : SearchResult()
//    object NotFound : SearchResult()
//}
