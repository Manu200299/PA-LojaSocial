package com.example.lojasocial.domain.use_case

import android.util.Log
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow

class GetBeneficiariesUseCase(private val repository: BeneficiaryRepository){
    suspend operator fun invoke(): Flow<List<Beneficiary>>{
        Log.d("UseCase", "Fetching beneficiaries...")
        return repository.getBeneficiaries()
    }
}

class AddBeneficiaryUseCase(private val repository: BeneficiaryRepository){
    suspend operator fun invoke(addBeneficiary: Beneficiary): Result<Unit>{
        Log.d("UseCase", "Adding beneficiary: $addBeneficiary")
        return repository.addBeneficiary(addBeneficiary)
    }
}

class BeneficiarySearchByPhoneNumberUseCase(private val repository: BeneficiaryRepository){
    suspend operator fun invoke(number: String): Flow<List<Beneficiary>>{
        Log.d("UseCase", "Searching for beneficiary phone number: $number")
        return repository.searchByPhoneNumber(number)
    }
}

class BeneficiarySearchByIdNumberUseCase(private val repository: BeneficiaryRepository){
    suspend operator fun invoke(id: String): Flow<List<Beneficiary>>{
        Log.d("UseCase", "Searching for beneficiary ID number: $id")
        return repository.searchByIdNumber(id)
    }
}

