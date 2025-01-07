package com.example.lojasocial.presentation.volunteers

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.R
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.domain.model.VolunteerLogin
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginVolunteerScreen(
    onNavigateBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    sessionManager: SessionManager

) {
    val viewModel: VolunteerViewModel = viewModel(
        factory = VolunteerViewModel.Factory(sessionManager)
    )

    var telefone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login Voluntário", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Entre na sua conta de voluntário", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Mostrar/Esconder senha"
                        )
                    }
                }
            )

            Button(
                onClick = {
                    val loginVolunteer = VolunteerLogin(
                        telefone = telefone,
                        password = password
                    )

                    if (telefone.isBlank() || password.isBlank()) {
                        // volunteerViewModel.setUiStateError("Por favor, preencha todos os campos.")
                        Log.e("LoginViewModel", "Please, fill all the required fields")
                    } else {
                        viewModel.loginVolunteer(loginVolunteer)

                    }
                },
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
                    Text(
                        text = (uiState as VolunteerViewModel.UiState.Error).message,
                        color = Color.Red
                    )
                }

                else -> {}
            }
        }
    }
}