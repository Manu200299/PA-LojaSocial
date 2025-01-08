import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.lojasocial.presentation.donations.DonationViewModel
import com.example.lojasocial.domain.model.Donation
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyDonationsScreen(
    onNavigateBack: () -> Unit,
    viewModel: DonationViewModel = viewModel() // ViewModel should be passed or created here
) {

    // Collect donations state from the ViewModel
    val donations by viewModel.donationsState.collectAsState()
    val errorMessage by viewModel.errorState.collectAsState()

    println("Donations in screen: $donations")
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
                // Show error message if exists
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
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
                                TableCell(donation.donorName, Modifier.weight(1f))
                                TableCell(donation.amount.toString(), Modifier.weight(1f))
                                TableCell(donation.description ?: "", Modifier.weight(1f))
                                TableCell(
                                    text = when (donation.donationType) {
                                        "Monetária" -> "${donation.amount}€"
                                        else -> donation.amount.toString()
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
