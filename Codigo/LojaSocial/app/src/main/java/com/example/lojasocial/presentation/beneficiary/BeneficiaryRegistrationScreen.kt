package com.example.lojasocial.presentation.beneficiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryRegistrationScreen(
    viewModel: BeneficiaryRegistrationViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    val registrationState by viewModel.registrationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = { Text("Registar Beneficiário", color = Color.White) },
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
                containerColor = Color(0xFF4052F7)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF4052F7),
                    unfocusedContainerColor = Color(0xFFD9D9D9),
                    focusedContainerColor = Color(0xFFD9D9D9)
                )
            )

            Button(
                onClick = {
                    viewModel.registerBeneficiary(nome.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4052F7)
                ),
                shape = MaterialTheme.shapes.medium,
                enabled = registrationState !is BeneficiaryRegistrationViewModel.RegistrationState.Loading
            ) {
                Text("Registar", style = MaterialTheme.typography.titleLarge)
            }

            when (registrationState) {
                is BeneficiaryRegistrationViewModel.RegistrationState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFF4052F7)
                    )
                }
                is BeneficiaryRegistrationViewModel.RegistrationState.Success -> {
                    Text(
                        "Beneficiário registrado com sucesso!",
                        color = Color(0xFF4052F7),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                is BeneficiaryRegistrationViewModel.RegistrationState.Error -> {
                    Text(
                        (registrationState as BeneficiaryRegistrationViewModel.RegistrationState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                else -> {}
            }
        }
    }
}