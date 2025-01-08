package com.example.lojasocial.domain.model

data class StockItem(
    val itemId: String,
    val nome: String,
    val quantidade: Int,
    val categoria: String,
    val isEssential: Boolean
)
