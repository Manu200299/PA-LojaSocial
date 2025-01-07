package com.example.lojasocial.presentation.stock

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.domain.model.StockItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItemScreen(
    onNavigateBack: () -> Unit,
    viewModel: StockViewModel = viewModel(factory = StockViewModel.Factory())
) {
    var itemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var initialQuantity by remember { mutableStateOf("0") }
    var isEssentialItem by remember { mutableStateOf(false) }

    // Exemplo de categorias
    val categories = listOf("Roupa", "Alimentos", "Higiene", "Casa", "Outros")

    // Controla se o menu está aberto (expanded) ou fechado
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Novo Item", color = Color.White) },
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Nome do Item
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Nome do Item") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF3851F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Categoria (DropdownMenu normal)
                    Box {
                        OutlinedTextField(
                            value = selectedCategory ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoria") },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expandir menu"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFD9D9D9),
                                focusedContainerColor = Color(0xFFD9D9D9),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF3851F1)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Quantidade Inicial
                    OutlinedTextField(
                        value = initialQuantity,
                        onValueChange = {
                            // Permitir apenas dígitos
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                initialQuantity = it
                            }
                        },
                        label = { Text("Quantidade Inicial") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF3851F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Checkbox para Item Essencial
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isEssentialItem,
                            onCheckedChange = { isEssentialItem = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF3851F1)
                            )
                        )
                        Text(
                            text = "Item Essencial",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val stockItem = StockItem(
                        nome = itemName,
                        quantidade = initialQuantity.toIntOrNull() ?: 0,
                        categoria = selectedCategory.orEmpty(),
                        isEssential = isEssentialItem
                    )
                    viewModel.addNewItem(stockItem)

                    // Depois de adicionar, podes navegar de volta ou limpar os campos
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3851F1)
                ),
                enabled = itemName.isNotBlank() && !selectedCategory.isNullOrEmpty()
            ) {
                Text(
                    "Adicionar Item",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
