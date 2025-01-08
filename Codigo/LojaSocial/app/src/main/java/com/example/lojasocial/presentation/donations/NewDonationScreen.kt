package com.example.lojasocial.presentation.donations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.domain.model.Donation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDonationScreen(
    onNavigateBack: () -> Unit,
    viewModel: DonationViewModel = viewModel(factory = DonationViewModel.Factory())
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedDonationType by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("") }
    var isMonetaryDonation by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val donationTypes = listOf("Roupa", "Alimentos", "Monetária", "Outros")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Doação", color = Color.White) },
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
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF3851F1)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF3851F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Phone Number Field
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Número Telefone") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF3851F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Donation Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedDonationType ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Doação") },
                            trailingIcon = {
                                Icon(Icons.Default.KeyboardArrowDown, "Expand dropdown menu")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFD9D9D9),
                                focusedContainerColor = Color(0xFFD9D9D9),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF3851F1)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            donationTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedDonationType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Description Field
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF3851F1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Monetary Donation Checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isMonetaryDonation,
                            onCheckedChange = { isMonetaryDonation = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF3851F1)
                            )
                        )
                        Text("Doação Monetária")
                    }

                    // Quantity Field
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantidade") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFD9D9D9),
                            focusedContainerColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF3851F1)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = if (isMonetaryDonation) {
                            { Text("€", modifier = Modifier.padding(end = 16.dp)) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Add Item Button
            Button(
                onClick = {
                    val newDonation = Donation(
                        donorName = name,
                        phoneNumber = phoneNumber,
                        donationType = selectedDonationType ?: "",
                        description = description,
                        amount = if (isMonetaryDonation) quantity.toDoubleOrNull() ?: 0.0 else 0.0, // Ensuring quantity is parsed to Double
                    )

                    viewModel.addNewDonation(newDonation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3851F1)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Adicionar Item",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}


