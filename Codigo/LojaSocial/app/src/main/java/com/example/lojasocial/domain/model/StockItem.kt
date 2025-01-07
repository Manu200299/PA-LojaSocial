package com.example.lojasocial.domain.model

data class StockItem(
    var itemId: String = "",
    var nome: String = "",
    var quantidade: Int = 0,
    var categoria: String = "",
    var isEssential: Boolean = false
    // adiciona aqui quaisquer outros campos que precises (descrição, etc.)
)
