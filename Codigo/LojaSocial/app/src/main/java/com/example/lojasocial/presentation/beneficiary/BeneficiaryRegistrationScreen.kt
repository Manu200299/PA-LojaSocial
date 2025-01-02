import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.LocalAppDependencies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryRegistrationScreen(
    viewModel: BeneficiaryRegistrationViewModel = viewModel(
        factory = BeneficiaryRegistrationViewModel.Factory(
            LocalAppDependencies.current.addBeneficiaryUseCase
        )
    ),
    onNavigateBack: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

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
                enabled = uiState !is BeneficiaryRegistrationViewModel.UiState.Loading
            ) {
                Text("Registar", style = MaterialTheme.typography.titleLarge)
            }

            when (uiState) {
                is BeneficiaryRegistrationViewModel.UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFF4052F7)
                    )
                }
                is BeneficiaryRegistrationViewModel.UiState.Success -> {
                    Text(
                        "Beneficiário registrado com sucesso!",
                        color = Color(0xFF4052F7),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                is BeneficiaryRegistrationViewModel.UiState.Error -> {
                    Text(
                        (uiState as BeneficiaryRegistrationViewModel.UiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                else -> {}
            }
        }
    }
}
