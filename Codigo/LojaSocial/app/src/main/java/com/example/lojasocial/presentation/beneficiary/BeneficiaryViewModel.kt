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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BeneficiaryViewModel(
    private val sessionManager: SessionManager,
//    private val addBeneficiaryUseCase: AddBeneficiaryUseCase
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private val api = FirebaseApi(auth, database)
    private val repository = BeneficiaryRepositoryImpl(api)

    // Use cases para BENEFICIARY
    private val addBeneficiaryUseCase = AddBeneficiaryUseCase(repository)
    private val getBeneficiariesUseCase = GetBeneficiariesUseCase(repository)
    private val searchByPhoneNumber = BeneficiarySearchByPhoneNumberUseCase(repository)
    private val searchByIdNumberUseCase = BeneficiarySearchByIdNumberUseCase(repository)

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun addBeneficiary(addBeneficiary: Beneficiary){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try{
                addBeneficiaryUseCase(addBeneficiary)
                _uiState.value = UiState.Success
                Log.d("BeneficiaryRegistrationViewModel", "Registering beneficiary: $addBeneficiary" )
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.message ?: "Unknown erro")
                Log.e("BeneficiaryRegistrationViewModel", "Error registering beneficiary! | ${e.message}")
            }
        }
    }

    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BeneficiaryViewModel::class.java)) {
                return BeneficiaryViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
}
