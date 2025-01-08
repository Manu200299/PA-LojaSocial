package com.example.lojasocial.domain.use_case

import com.example.lojasocial.domain.model.StockItem
import com.example.lojasocial.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow

class AddStockItemUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(stockItem: StockItem): Result<Unit> {
        return repository.addStockItem(stockItem)
    }
}

class GetStockItemsUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(): Flow<List<StockItem>> {
        return repository.getStockItems()
    }
}

class UpdateStockItemUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(stockItem: StockItem): Result<Unit> {
        return repository.updateStockItem(stockItem)
    }
}

class DeleteStockItemUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(itemId: String): Result<Unit> {
        return repository.deleteStockItem(itemId)
    }
}
