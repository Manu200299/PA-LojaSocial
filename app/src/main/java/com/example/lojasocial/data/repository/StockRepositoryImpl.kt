package com.example.lojasocial.data.repository

import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.remote.model.toStockItemDto
import com.example.lojasocial.domain.model.StockItem
import com.example.lojasocial.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockRepositoryImpl(
    private val firebaseApi: FirebaseApi
) : StockRepository {

    override suspend fun addStockItem(stockItem: StockItem): Result<Unit> {
        return firebaseApi.addStockItem(stockItem.toStockItemDto())
    }

    override suspend fun getStockItems(): Flow<List<StockItem>> {
        return firebaseApi.getStockItems().map { dtoList ->
            dtoList.map { it.toStockItem() }
        }
    }

    override suspend fun updateStockItem(stockItem: StockItem): Result<Unit> {
        return firebaseApi.updateStockItem(stockItem.toStockItemDto())
    }

    override suspend fun deleteStockItem(itemId: String): Result<Unit> {
        return firebaseApi.deleteStockItem(itemId)
    }

}
