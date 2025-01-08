package com.example.lojasocial.presentation.visit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.domain.model.StockItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitStockSelectionScreen(
    onNavigateToReview: () -> Unit,
    onNavigateBack: () -> Unit,
    sessionManager: SessionManager,
    beneficiaryId: String
) {
    val viewModel: VisitViewModel = viewModel(
        factory = VisitViewModel.Factory(sessionManager)
    )

    val stockItems by viewModel.stockItems.collectAsState()
    val currentVisit by viewModel.currentVisit.collectAsState()
    val filteredStockItems by viewModel.filteredStockItems.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(beneficiaryId) {
        viewModel.loadStockItems()
        viewModel.checkActiveVisit(beneficiaryId)
        viewModel.loadVisit(beneficiaryId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Escolher Itens",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF3851F1)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Active Session Title
            Text(
                text = "Sessão Ativa: Beneficiário",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Search and Filter Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF3851F1))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Pesquisar itens...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF0A090B)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Button(
                        onClick = { viewModel.updateSearchQuery(searchQuery) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Pesquisar", color = Color(0xFFFFFFFF))
                    }

                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = showCategoryDropdown,
                        onExpandedChange = { showCategoryDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory ?: "Selecionar categoria",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFD9D9D9),
                                focusedContainerColor = Color(0xFFD9D9D9)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        viewModel.updateSelectedCategory(category)
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            when (uiState) {
                is VisitViewModel.UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFF3851F1)
                    )
                }

                is VisitViewModel.UiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(stockItems) { item ->
                            val quantity =
                                currentVisit?.items?.find { it.stockItemId == item.itemId }?.quantity
                                    ?: 0
                            StockItemCard(
                                item = item,
                                quantity = quantity,
                                onAddItem = { viewModel.addItemToVisit(item.itemId, 1) },
                                onRemoveItem = { viewModel.removeItemFromVisit(item.itemId) },
                                isMaxQuantity = quantity >= item.quantidade
                                )
                        }
                    }

                    Button(
                        onClick = onNavigateToReview,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Rever Visita", color = Color.White)
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
}

@Composable
fun StockItemCard(
    item: StockItem,
    quantity: Int,
    onAddItem: () -> Unit,
    onRemoveItem: () -> Unit,
    isMaxQuantity: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF3851F1))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.nome,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = item.categoria,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            if (item.isEssential) {
                Text(
                    text = "Essencial",
                    color = Color(0xFFFF0000),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Quantidade: ${item.quantidade}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF000000)
                    )
                    Text(
                        "Selecionado: $quantity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF000000)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botao de menos
                    if (quantity > 0) {
                        IconButton(
                            onClick = onRemoveItem,
                            modifier = Modifier
                                .size(66.dp)
                                .background(
                                    color = Color(0xFFFF0000),
                                    shape = RoundedCornerShape(62.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = "Remove",
                                tint = Color.White
                            )
                        }
                    }
                    // Botao de add
                    IconButton(
                        onClick = onAddItem,
                        enabled = !isMaxQuantity,
                        modifier = Modifier
                            .size(66.dp)
                            .background(
                                color = if (isMaxQuantity) Color(0xFFCCCCCC) else Color(0xFF00FF26),
                                shape = RoundedCornerShape(62.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = if (isMaxQuantity) Color(0xFF666666) else Color(0xFFFFFFFF),
                        )
                    }
                }
            }
        }
    }
}


