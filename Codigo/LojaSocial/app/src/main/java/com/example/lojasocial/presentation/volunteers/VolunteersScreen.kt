package com.example.lojasocial.presentation.volunteers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.domain.model.Volunteer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteersScreen(
    sessionManager: SessionManager,
    onNavigateBack: () -> Unit = {}
) {
    // Cria o ViewModel usando o sessionManager
    val viewModel: VolunteerViewModel = viewModel(
        factory = VolunteerViewModel.Factory(sessionManager)
    )

    val uiState by viewModel.uiState.collectAsState()
    val volunteersList by viewModel.volunteersListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllVolunteers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestão de Voluntários", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3851F1))
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
            if (uiState is VolunteerViewModel.UiState.Loading) {
                CircularProgressIndicator()
            }

            if (uiState is VolunteerViewModel.UiState.Error) {
                Text(
                    text = (uiState as VolunteerViewModel.UiState.Error).message,
                    color = Color.Red
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(volunteersList) { volunteer ->
                    VolunteerRow(
                        volunteer = volunteer,
                        onEditClick = { updated ->
                            viewModel.updateVolunteer(updated)
                        },
                        onDeleteClick = { volunteerId ->
                            viewModel.deleteVolunteer(volunteerId)
                        }
                    )
                }
            }

            Button(
                onClick = { viewModel.getAllVolunteers() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1))
            ) {
                Text(
                    text = "Recarregar Lista",
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun VolunteerRow(
    volunteer: Volunteer,
    onEditClick: (Volunteer) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Info do voluntário
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = volunteer.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = "Telefone: ${volunteer.telefone}")
                Text(text = "Escala: ${volunteer.escala}")
                Text(text = "Tipo: ${volunteer.tipo}")
            }

            // Botões de Editar e Apagar
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { showEditDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }

                IconButton(
                    onClick = {
                        onDeleteClick(volunteer.volunteerId)
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Apagar")
                }
            }
        }
    }
    if (showEditDialog) {
        EditVolunteerDialog(
            volunteer = volunteer,
            onDismiss = { showEditDialog = false },
            onSave = { updatedVolunteer ->
                // Quando guardamos, chamamos onEditClick
                onEditClick(updatedVolunteer)
                // E fechamos o diálogo
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditVolunteerDialog(
    volunteer: Volunteer,
    onDismiss: () -> Unit,
    onSave: (Volunteer) -> Unit
) {
    // Estados temporários para nome, telefone, escala, etc.
    var nome by remember { mutableStateOf(volunteer.nome) }
    var telefone by remember { mutableStateOf(volunteer.telefone) }
    var escala by remember { mutableStateOf(volunteer.escala) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Voluntário") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = escala,
                    onValueChange = { escala = it },
                    label = { Text("Escala") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Se houver mais campos, adiciona aqui
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Construir um novo voluntário com os campos editados
                val updatedVolunteer = volunteer.copy(
                    nome = nome,
                    telefone = telefone,
                    escala = escala
                )
                onSave(updatedVolunteer)  // devolver voluntário atualizado
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


