package com.example.lojasocial.presentation.statistics
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import com.example.lojasocial.presentation.statistics.components.BarChart
import com.example.lojasocial.presentation.statistics.components.LineChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    beneficiaryRepository: BeneficiaryRepository
) {
    val viewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModel.Factory(beneficiaryRepository)
    )
    val nationalitiesData by viewModel.nationalitiesData.collectAsState()
    val monthlyData by viewModel.monthlyData.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dados Estatísticos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Nacionalidades", style = MaterialTheme.typography.titleMedium)
            BarChart(nationalitiesData)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Procura da Loja", style = MaterialTheme.typography.titleMedium)
            LineChart(monthlyData)
        }
    }
}