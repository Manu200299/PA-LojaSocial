package com.example.lojasocial.presentation.beneficiary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.LocalAppDependencies
import com.example.lojasocial.data.model.Beneficiary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryCard(
    beneficiary: Beneficiary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = beneficiary.nome,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Telefone: ${beneficiary.telefone}")
            Text("Nº Identificação: ${beneficiary.numeroIdentificacao}")
            Text("Cidade: ${beneficiary.cidade}")
            Text("Prioridade: ${beneficiary.prioridade}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInBeneficiaryScreen(
    viewModel: CheckInBeneficiaryViewModel = viewModel(
        factory = CheckInBeneficiaryViewModel.Factory(
            LocalAppDependencies.current.searchBeneficiariesUseCase
        )
    ),
    onNavigateBack: () -> Unit = {},
    onBeneficiarySelected: (Beneficiary) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var expandedDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Check-In Beneficiários", color = Color.White) },
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

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            // Family Search Card
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .border(
//                        width = 1.dp,
//                        color = Color(0xFF3851F1),
//                        shape = RoundedCornerShape(12.dp)
//                    ),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White
//                )
//            ) {
//                Column(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                ) {
//                    Text(
//                        "Pesquisar por Familia",
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    OutlinedTextField(
//                        value = uiState.familySearchQuery,
//                        onValueChange = { viewModel.onFamilySearchQueryChange(it) },
//                        modifier = Modifier.fillMaxWidth(),
//                        placeholder = { Text("Escolher familia...") },
//                        leadingIcon = {
//                            Icon(
//                                Icons.Default.Search,
//                                contentDescription = null,
//                                tint = Color.Gray
//                            )
//                        },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color(0xFFD9D9D9),
//                            focusedContainerColor = Color(0xFFD9D9D9)
//                        ),
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                }
//            }

            // Number Search Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFF3851F1),
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Pesquisar por Número Telemóvel",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Search Type Dropdown
                        ExposedDropdownMenuBox(
                            expanded = expandedDropdown,
                            onExpandedChange = { expandedDropdown = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = when (uiState.selectedSearchType) {
                                    SearchType.PHONE -> "Nº Telefone"
                                    SearchType.ID -> "Nº Cidadão/Passaporte"
                                },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFD9D9D9),
                                    focusedContainerColor = Color(0xFFD9D9D9)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Nº Telefone") },
                                    onClick = {
                                        viewModel.onSearchTypeChange(SearchType.PHONE)
                                        expandedDropdown = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Nº Cidadão/Passaporte") },
                                    onClick = {
                                        viewModel.onSearchTypeChange(SearchType.ID)
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }

                        // Search Input
                        OutlinedTextField(
                            value = uiState.numberSearchQuery,
                            onValueChange = { viewModel.onNumberSearchQueryChange(it) },
                            modifier = Modifier.weight(1f),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            placeholder = { Text("Pesquisar") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFD9D9D9),
                                focusedContainerColor = Color(0xFFD9D9D9)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            // Search results e o botao procurar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                // Search Results
                when (val searchResult = uiState.searchResult) {
                    is SearchResult.Found -> {
                        Text(
                            "Beneficiários encontrados: ${searchResult.beneficiaries.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .weight(1f)
                        ){
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(searchResult.beneficiaries) { beneficiary ->
                                    BeneficiaryCard(
                                        beneficiary = beneficiary,
                                        onClick = { onBeneficiarySelected(beneficiary)})
                                }
                            }
                        }
                    }
                    SearchResult.NotFound -> {
                        Text(
                            "Beneficiário não encontrado",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFF44336),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    null -> {
                        // No search performed yet
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Search Button
                Button(
                    onClick = { viewModel.performSearch() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3851F1)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Pesquisar", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
fun BeneficiaryCard(beneficiary: Beneficiary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = beneficiary.nome,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Telefone: ${beneficiary.telefone}")
            Text("Nº Identificação: ${beneficiary.numeroIdentificacao}")
            Text("Cidade: ${beneficiary.cidade}")
            Text("Prioridade: ${beneficiary.prioridade}")
        }
    }
}
