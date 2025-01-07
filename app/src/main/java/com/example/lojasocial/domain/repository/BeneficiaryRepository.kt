package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Beneficiary
import kotlinx.coroutines.flow.Flow

// Repositorio beneficiarios camada domain
interface  BeneficiaryRepository {
    suspend fun addBeneficiary(addBeneficiary: Beneficiary): Result<Unit>
    suspend fun getBeneficiaries(): Flow<List<Beneficiary>>
    suspend fun getBeneficiaryById(beneficiaryId: String): Beneficiary?
    suspend fun searchByPhoneNumber(number: String): Flow<List<Beneficiary>>
    suspend fun searchByIdNumber(id: String): Flow<List<Beneficiary>>
}

