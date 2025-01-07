package com.example.lojasocial.presentation.stock

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lojasocial.presentation.stock.components.StockManagementCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockManagementScreen(
    onNavigateBack: () -> Unit,
    onInventoryClick: () -> Unit,
    onAddItemClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestão de Stock", color = Color.White) },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StockManagementCard(
                    icon = Icons.Outlined.Search,
                    text = "Inventário",
                    onClick = onInventoryClick,
                    modifier = Modifier.weight(1f)
                )

                StockManagementCard(
                    icon = Icons.Default.Add,
                    text = "Adicionar\nNovo Item",
                    onClick = onAddItemClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
