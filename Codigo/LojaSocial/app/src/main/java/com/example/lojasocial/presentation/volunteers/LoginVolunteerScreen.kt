package com.example.lojasocial.presentation.volunteers

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.LocalAppDependencies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginVolunteerScreen(
    onNavigateBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val volunteerViewModel: VolunteerViewModel = viewModel(
        factory = VolunteerViewModel.Factory(
            registerUseCase = LocalAppDependencies.current.registerVolunteerUseCase,
            loginUseCase = LocalAppDependencies.current.loginVolunteerUseCase
        )
    )

    val uiState by volunteerViewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login Voluntário", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Entre na sua conta de voluntário", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Default.ArrowBack else Icons.Default.ArrowBack
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = icon, contentDescription = "Mostrar/Esconder senha")
                    }
                }
            )

            Button(
                onClick = { volunteerViewModel.loginVolunteer(email, senha) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is VolunteerViewModel.UiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1))
            ) {
                Text("Entrar", color = Color.White)
            }

            when (uiState) {
                is VolunteerViewModel.UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is VolunteerViewModel.UiState.Success -> {
                    LaunchedEffect(Unit) {
                        onLoginSuccess()
                    }
                }
                is VolunteerViewModel.UiState.Error -> {
                    Text("Erro ao fazer login. Verifique suas credenciais.", color = Color.Red)
                }
                else -> {}
            }
        }
    }
}
