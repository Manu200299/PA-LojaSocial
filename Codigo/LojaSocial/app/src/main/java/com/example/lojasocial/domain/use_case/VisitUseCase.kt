package com.example.lojasocial.domain.use_case

import android.util.Log
import androidx.compose.runtime.referentialEqualityPolicy
import com.example.lojasocial.domain.model.Visit
import com.example.lojasocial.domain.repository.VisitRepository
import kotlinx.coroutines.flow.Flow

class CreateVisitUseCase(private val repository: VisitRepository) {
    suspend operator fun invoke(visit: Visit): Result<Visit>{
        Log.d("UseCase", "Creating visit...")
        return repository.createVisit(visit)
    }
}

class UpdateVisitUseCase(private val repository: VisitRepository){
    suspend operator fun invoke(visit: Visit): Result<Unit>{
        Log.d("UseCase", "Updating visit...")
        return repository.updateVisit(visit)
    }
}

class GetVisitsByBeneficiaryId(private val repository: VisitRepository) {
    suspend operator fun invoke(beneficiaryId: String): Flow<List<Visit>>{
        Log.d("UseCase", "Fetching visits for beneficiaryId: $beneficiaryId ...")
        return repository.getVisitsByBeneficiaryId(beneficiaryId)
    }
}