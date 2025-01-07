package com.example.lojasocial.presentation.stock

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.domain.model.StockItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onNavigateBack: () -> Unit,
    // Se quiseres fazer algo quando clicas no "Guardar"
    onSave: () -> Unit = {},
    viewModel: StockViewModel = viewModel(factory = StockViewModel.Factory())
) {
    val stockItems by viewModel.stockItemsState.collectAsState()
    val errorMsg by viewModel.errorState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventário", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3851F1)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, Color(0xFF3851F1))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(stockItems) { item ->
                        InventoryItemRow(
                            item = item,
                            onIncrement = { stockItem ->
                                val updated = stockItem.copy(quantidade = stockItem.quantidade + 1)
                                viewModel.updateItem(updated)
                            },
                            onDecrement = { stockItem ->
                                if (stockItem.quantidade > 0) {
                                    val updated = stockItem.copy(quantidade = stockItem.quantidade - 1)
                                    viewModel.updateItem(updated)
                                }
                            },
                            onDelete = { stockItem ->
                                viewModel.deleteItem(stockItem.itemId)
                            }
                        )
                    }
                }
            }

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3851F1)
                )
            ) {
                Text(
                    "Guardar",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }

            // Mensagem de erro, se existir
            errorMsg?.let { msg ->
                Text(msg, color = Color.Red)
            }
        }
    }
}

@Composable
fun InventoryItemRow(
    item: StockItem,
    onIncrement: (StockItem) -> Unit,
    onDecrement: (StockItem) -> Unit,
    onDelete: (StockItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Informação do item
        Column(modifier = Modifier.weight(1f)) {
            // Se quiseres destacar itens que estejam a 0 ou que sejam "isEssential", podes mudar a cor
            Text(
                text = item.nome,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Quantidade: ${item.quantidade}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            if (item.categoria.isNotBlank()) {
                Text(
                    text = "Categoria: ${item.categoria}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (item.isEssential) {
                Text(
                    text = "Item Essencial",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }

        // Botões: Decrementar, Incrementar, e Eliminar
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onDecrement(item) },
                modifier = Modifier
                    .background(Color(0xFFFF0000), RoundedCornerShape(8.dp))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Diminuir",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = { onIncrement(item) },
                modifier = Modifier
                    .background(Color(0xFF00FF26), RoundedCornerShape(8.dp))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Aumentar",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = { onDelete(item) },
                modifier = Modifier
                    .background(Color(0xFF3851F1), RoundedCornerShape(8.dp))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Apagar",
                    tint = Color.White
                )
            }
        }
    }
}
