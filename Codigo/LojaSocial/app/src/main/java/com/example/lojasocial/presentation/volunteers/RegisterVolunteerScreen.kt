package com.example.lojasocial.presentation.volunteers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.LocalAppDependencies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVolunteerScreen(
    viewModel: VolunteerViewModel = viewModel(
        factory = VolunteerViewModel.Factory(
            registerUseCase = LocalAppDependencies.current.registerVolunteerUseCase,
            loginUseCase = LocalAppDependencies.current.loginVolunteerUseCase
        )
    ),
    onNavigateBack: () -> Unit = {}
) {
    // Campos para armazenar os valores inseridos pelo usuário
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Obter o estado atual da UI
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Voluntário", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
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
            // Campo Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            // Campo Telefone
            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Campo Data de Nascimento
            OutlinedTextField(
                value = dataNascimento,
                onValueChange = { dataNascimento = it },
                label = { Text("Data de Nascimento") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Campo Senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Campo Confirmar Senha
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Botão Registrar
            Button(
                onClick = {
                    viewModel.registerVolunteer(
                        nome.trim(),
                        email.trim(),
                        telefone.trim(),
                        password.trim(),
                        confirmPassword.trim(),
                        dataNascimento.trim()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is VolunteerViewModel.UiState.Loading
            ) {
                Text("Registrar")
            }

            // Estado da UI
            when (uiState) {
                is VolunteerViewModel.UiState.Success -> {
                    Text("Registro bem-sucedido!", color = MaterialTheme.colorScheme.primary)
                }
                is VolunteerViewModel.UiState.Error -> {
                    Text(
                        (uiState as VolunteerViewModel.UiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {}
            }
        }
    }
}
