package com.example.lojasocial.domain.use_case

import com.example.lojasocial.data.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import java.util.UUID
import javax.inject.Inject

// Use case para adicionar beneficiarios
class AddBeneficiaryUseCase(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(nome: String): Result<Unit> {
        val beneficiary = Beneficiary(id = UUID.randomUUID().toString(), nome = nome)
        return repository.addBeneficiary(beneficiary)
    }
}

