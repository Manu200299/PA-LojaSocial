package com.example.lojasocial.domain.repository

import com.example.lojasocial.data.model.Beneficiary
import kotlinx.coroutines.flow.Flow

// Repositorio beneficiarios camada domain
interface BeneficiaryRepository {
    suspend fun addBeneficiary(beneficiary: Beneficiary): Result<Unit>
    fun getBeneficiaries(): Flow<List<Beneficiary>>
}

