package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.StockItem
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun addStockItem(stockItem: StockItem): Result<Unit>
    suspend fun getStockItems(): Flow<List<StockItem>>
    suspend fun updateStockItem(stockItem: StockItem): Result<Unit>
    suspend fun deleteStockItem(itemId: String): Result<Unit>
}
