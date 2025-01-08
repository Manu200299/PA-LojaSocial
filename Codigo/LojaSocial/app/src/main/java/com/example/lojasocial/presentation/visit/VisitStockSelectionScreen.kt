package com.example.lojasocial.presentation.visit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.domain.model.StockItem

@Composable
fun VisitStockSelectionScreen(
    onNavigateToReview: () -> Unit,
    sessionManager: SessionManager
) {
    val viewModel: VisitViewModel = viewModel(
        factory = VisitViewModel.Factory(sessionManager)
    )

    val stockItems by viewModel.stockItems.collectAsState()
    val currentVisit by viewModel.currentVisit.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Selecionar Itens",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF3851F1)
        )

        when (uiState) {
            is VisitViewModel.UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF3851F1)
                )
            }
            is VisitViewModel.UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(stockItems) { item ->
                        StockItemCard(
                            item = item,
                            quantity = currentVisit?.items?.find { it.stockItemId == item.itemId }?.quantity ?: 0,
                            onAddItem = { viewModel.addItemToVisit(item.itemId, 1) },
                            onRemoveItem = { viewModel.removeItemFromVisit(item.itemId) }
                        )
                    }
                }

                Button(
                    onClick = onNavigateToReview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Revisar Visita", color = Color.White)
                }
            }
            is VisitViewModel.UiState.Error -> {
                Text(
                    (uiState as VisitViewModel.UiState.Error).message,
                    color = Color(0xFFFF0000)
                )
            }
            else -> {}
        }
    }
}

@Composable
fun StockItemCard(
    item: StockItem,
    quantity: Int,
    onAddItem: () -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF7FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nome, style = MaterialTheme.typography.titleMedium)
                Text("Disponível: ${item.quantidade}", style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRemoveItem) {
                    Icon(Icons.Default.Remove, contentDescription = "Remove")
                }
                Text(quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onAddItem) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}
