package com.example.lojasocial

import androidx.compose.runtime.compositionLocalOf
import com.example.lojasocial.data.remote.FirebaseApi
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import com.example.lojasocial.domain.use_case.AddBeneficiaryUseCase
import com.example.lojasocial.domain.use_case.SearchBeneficiaryUseCase
import com.google.firebase.database.FirebaseDatabase

// Dependencias
class AppDependencies {
    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val firebaseApi: FirebaseApi by lazy { FirebaseApi(firebaseDatabase) }
    private val beneficiaryRepository: BeneficiaryRepository by lazy { BeneficiaryRepositoryImpl(firebaseApi) }
    val addBeneficiaryUseCase: AddBeneficiaryUseCase by lazy { AddBeneficiaryUseCase(beneficiaryRepository) }
    val searchBeneficiariesUseCase: SearchBeneficiaryUseCase by lazy { SearchBeneficiaryUseCase(beneficiaryRepository) }
}

val LocalAppDependencies = compositionLocalOf<AppDependencies> { error("AppDependencies not provided") }

