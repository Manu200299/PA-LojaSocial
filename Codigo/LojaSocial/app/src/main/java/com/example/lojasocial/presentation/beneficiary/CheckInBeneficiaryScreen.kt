package com.example.lojasocial.presentation.beneficiary

import android.app.appsearch.SearchResult
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
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.model.BeneficiaryDto
import com.example.lojasocial.domain.model.Beneficiary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInBeneficiaryScreen(
    sessionManager: SessionManager,
    viewModel: BeneficiaryViewModel = viewModel(
        factory = BeneficiaryViewModel.Factory(sessionManager)
    ),
    onNavigateBack: () -> Unit = {},
    onBeneficiarySelected: (Beneficiary) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchType by viewModel.searchType.collectAsState()
    var expandedDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                        "Pesquisar Beneficiário",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedDropdown,
                            onExpandedChange = { expandedDropdown = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = when (searchType) {
                                    BeneficiaryViewModel.SearchType.PHONE -> "Nº Telefone"
                                    BeneficiaryViewModel.SearchType.ID -> "Nº Cidadão/Passaporte"
                                },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown)
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
                                        viewModel.onSearchTypeChange(BeneficiaryViewModel.SearchType.PHONE)
                                        expandedDropdown = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Nº Cidadão/Passaporte") },
                                    onClick = {
                                        viewModel.onSearchTypeChange(BeneficiaryViewModel.SearchType.ID)
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
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

            when (uiState) {
                is BeneficiaryViewModel.UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFF3851F1)
                    )
                }
                is BeneficiaryViewModel.UiState.Success -> {
                    Text(
                        "Beneficiários encontrados: ${searchResults.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF4CAF50)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { beneficiary ->
                            BeneficiaryCard(
                                beneficiary = beneficiary,
                                onClick = { onBeneficiarySelected(beneficiary) }
                            )
                        }
                    }
                }
                is BeneficiaryViewModel.UiState.NotFound -> {
                    Text(
                        "Nenhum beneficiário encontrado",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFF44336)
                    )
                }
                is BeneficiaryViewModel.UiState.Error -> {
                    Text(
                        "Erro: ${(uiState as BeneficiaryViewModel.UiState.Error).message}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFF44336)
                    )
                }
                else -> {
                    // Idle state, no search performed yet
                }
            }

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