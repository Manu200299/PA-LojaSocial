package com.example.lojasocial

import androidx.compose.runtime.compositionLocalOf
import com.example.lojasocial.data.remote.FirebaseApi
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.data.repository.VolunteerRepositoryImpl
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import com.example.lojasocial.domain.repository.VolunteerRepository
import com.example.lojasocial.domain.use_case.AddBeneficiaryUseCase
import com.example.lojasocial.domain.use_case.LoginVolunteerUseCase
import com.example.lojasocial.domain.use_case.RegisterVolunteerUseCase
import com.example.lojasocial.domain.use_case.SearchBeneficiaryUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AppDependencies {

    // Firebase inicializações
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    // API remota
    private val firebaseApi: FirebaseApi by lazy { FirebaseApi(auth = firebaseAuth, database = firebaseDatabase) }

    // Repositórios
    private val beneficiaryRepository: BeneficiaryRepository by lazy { BeneficiaryRepositoryImpl(firebaseApi) }
    private val volunteerRepository: VolunteerRepository by lazy { VolunteerRepositoryImpl(firebaseApi) }

    // Casos de uso
    val addBeneficiaryUseCase: AddBeneficiaryUseCase by lazy { AddBeneficiaryUseCase(beneficiaryRepository) }
    val searchBeneficiariesUseCase: SearchBeneficiaryUseCase by lazy { SearchBeneficiaryUseCase(beneficiaryRepository) }
    val loginVolunteerUseCase: LoginVolunteerUseCase by lazy { LoginVolunteerUseCase(volunteerRepository) }
    val registerVolunteerUseCase: RegisterVolunteerUseCase by lazy { RegisterVolunteerUseCase(volunteerRepository) }
}

// Provedor de dependências no escopo da composição
val LocalAppDependencies = compositionLocalOf<AppDependencies> { error("AppDependencies not provided") }
