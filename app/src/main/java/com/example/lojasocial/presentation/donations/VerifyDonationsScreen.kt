package com.example.lojasocial.presentation.donations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyDonationsScreen(
    onNavigateBack: () -> Unit,
    donations: List<Donation> = sampleDonations // For demonstration
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verificar Doações", color = Color.White) },
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
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            border = BorderStroke(1.dp, Color(0xFF3851F1)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Table Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TableHeader("Name", Modifier.weight(1f))
                    TableHeader("Nº Telemóvel", Modifier.weight(1f))
                    TableHeader("Tipo Doação", Modifier.weight(1f))
                    TableHeader("Descrição", Modifier.weight(1f))
                    TableHeader("Quantidade", Modifier.weight(0.8f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Table Content
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(donations) { donation ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(donation.name, Modifier.weight(1f))
                            TableCell(donation.phoneNumber, Modifier.weight(1f))
                            TableCell(donation.type, Modifier.weight(1f))
                            TableCell(donation.description ?: "", Modifier.weight(1f))
                            TableCell(
                                text = when {
                                    donation.type == "Monetária" -> "${donation.quantity}€"
                                    else -> donation.quantity.toInt().toString()
                                },
                                modifier = Modifier.weight(0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TableHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(8.dp),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun TableCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

data class Donation(
    val name: String,
    val phoneNumber: String,
    val type: String,
    val description: String?,
    val quantity: Number
)

// Sample data for demonstration
private val sampleDonations = listOf(
    Donation("Doador X", "912 345 678", "Monetária", null, 99999),
    Donation("Doador Y", "919 876 543", "Monetária", null, 99999),
    Donation("Doador Z", "910 953 135", "Roupa", "Fábrica T\nShirts", 100)
)