package com.example.lojasocial.presentation.donations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.DonationRepositoryImpl
import com.example.lojasocial.domain.model.Donation
import com.example.lojasocial.domain.repository.DonationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DonationViewModel: ViewModel() {

    // Firebase initialization
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseApi = FirebaseApi(firebaseAuth, firebaseDatabase)

    // Donation repository initialized with Firebase API
    private val repository = DonationRepositoryImpl(firebaseApi)

    // State for holding the list of donations
    private val _donationsState = MutableStateFlow<List<Donation>>(emptyList())
    val donationsState: StateFlow<List<Donation>> = _donationsState.asStateFlow()

    // Error state to handle any errors
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()
    init {
        loadDonations()
    }

    /**
     * Loads donations from Firebase and updates the state.
     */
    private fun loadDonations() {
        viewModelScope.launch {
            repository.getDonations()
                .catch { e ->
                    _errorState.value = e.message ?: "Error loading donations."
                }
                .collect { donations ->
                    if (donations.isEmpty()) {
                        _errorState.value = "No donations available."
                    } else {
                        _donationsState.value = donations
                    }
                }
        }
    }

    /**
     * Adds a new donation to Firebase.
     */
    fun addNewDonation(donation: Donation) {
        viewModelScope.launch {
            val result = repository.addDonation(donation)
            result.onFailure { e ->
                _errorState.value = e.message ?: "Error adding donation."
            }
            loadDonations()  // Reload donations after adding
        }
    }

    /**
     * Updates an existing donation in Firebase.
     */
    fun updateDonation(donation: Donation) {
        viewModelScope.launch {
            val result = repository.updateDonation(donation)
            result.onFailure { e ->
                _errorState.value = e.message ?: "Error updating donation."
            }
            loadDonations()  // Reload donations after updating
        }
    }

    /**
     * Deletes a donation from Firebase by its ID.
     */
    fun deleteDonation(donationId: String) {
        viewModelScope.launch {
            val result = repository.deleteDonation(donationId)
            result.onFailure { e ->
                _errorState.value = e.message ?: "Error deleting donation."
            }
            loadDonations()  // Reload donations after deletion
        }
    }

    // Factory pattern for ViewModel if you're using dependency injection
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DonationViewModel::class.java)) {
                return DonationViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
