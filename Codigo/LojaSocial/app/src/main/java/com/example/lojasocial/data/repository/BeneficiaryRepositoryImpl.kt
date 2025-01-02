package com.example.lojasocial.data.repository

import com.example.lojasocial.data.model.Beneficiary
import com.example.lojasocial.data.remote.FirebaseApi
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow

// Repositorio beneficiario da camada de dados
class BeneficiaryRepositoryImpl(
    private val firebaseApi: FirebaseApi
) : BeneficiaryRepository {

    // Funcao para adicionar beneficiario
    override suspend fun addBeneficiary(beneficiary: Beneficiary): Result<Unit> {
        return firebaseApi.addBeneficiary(beneficiary)
    }

    // Funcao para extrair beneficiarios da firebase
    override fun getBeneficiaries(): Flow<List<Beneficiary>> {
        return firebaseApi.getBeneficiaries()
    }
}
