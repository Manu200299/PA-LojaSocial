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
    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val firebaseApi: FirebaseApi by lazy { FirebaseApi(firebaseDatabase) }

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Repositórios
    private val beneficiaryRepository: BeneficiaryRepository by lazy { BeneficiaryRepositoryImpl(firebaseApi) }
    private val volunteerRepository: VolunteerRepository by lazy { VolunteerRepositoryImpl(firebaseAuth) }

    // Casos de uso
    val addBeneficiaryUseCase: AddBeneficiaryUseCase by lazy { AddBeneficiaryUseCase(beneficiaryRepository) }
    val searchBeneficiariesUseCase: SearchBeneficiaryUseCase by lazy { SearchBeneficiaryUseCase(beneficiaryRepository) }
    val loginVolunteerUseCase: LoginVolunteerUseCase by lazy { LoginVolunteerUseCase(volunteerRepository) }
    val registerVolunteerUseCase: RegisterVolunteerUseCase by lazy { RegisterVolunteerUseCase(volunteerRepository) }
}

val LocalAppDependencies = compositionLocalOf<AppDependencies> { error("AppDependencies not provided") }
