import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.domain.use_case.AddBeneficiaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BeneficiaryRegistrationViewModel(
    private val addBeneficiaryUseCase: AddBeneficiaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun registerBeneficiary(nome: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = addBeneficiaryUseCase(nome)
            _uiState.value = when {
                result.isSuccess -> UiState.Success
                else -> UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    class Factory(private val addBeneficiaryUseCase: AddBeneficiaryUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BeneficiaryRegistrationViewModel::class.java)) {
                return BeneficiaryRegistrationViewModel(addBeneficiaryUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
