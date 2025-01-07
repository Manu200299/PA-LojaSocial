import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.domain.model.Beneficiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryRegistrationScreen(
//    viewModel: BeneficiaryRegistrationViewModel = viewModel(
//        factory = BeneficiaryRegistrationViewModel.Factory(
//            LocalAppDependencies.current.addBeneficiaryUseCase
//        )
//    ),
//    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    sessionManager: SessionManager
) {
    val viewModel: BeneficiaryViewModel = viewModel(
        factory = BeneficiaryViewModel.Factory(sessionManager)
    )

    var nome by remember { mutableStateOf("") }
    var nacionalidade by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var numeroIdentificacao by remember { mutableStateOf("") }
    var freguesia by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var contadorVisitas by remember { mutableStateOf(0) }
    var prioridade by remember { mutableStateOf("nenhuma") }
    var escola by remember { mutableStateOf("") }
    var anoEscolar by remember { mutableStateOf("") }
    var selectedFamily by remember { mutableStateOf<String?>(null) }
    var selectedAssociation by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsState()

    // Valores de test para Associacao e Familia
    val familias = listOf("Familia Silva", "Familia Santos", "Familia Mohamed", "Familia Baljeet")
    val associacoes = listOf("Associação A", "Associação B", "Associação C")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registar Voluntário", color = Color.White) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                // Coluna da esquerda
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome"
                    )

                    CustomTextField(
                        value = dataNascimento,
                        onValueChange = { dataNascimento = it },
                        label = "Data Nascimento",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    CustomTextField(
                        value = nacionalidade,
                        onValueChange = { nacionalidade = it },
                        label = "Nacionalidade"
                    )

                    CustomTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = "Telefone",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    CustomTextField(
                        value = numeroIdentificacao,
                        onValueChange = { numeroIdentificacao = it },
                        label = "Nº Cidadão/Passaporte"
                    )

                    CustomTextField(
                        value = freguesia,
                        onValueChange = { freguesia = it },
                        label = "Freguesia"
                    )

                    CustomTextField(
                        value = cidade,
                        onValueChange = { cidade = it },
                        label = "Cidade"
                    )
                }

                // Coluna da direita
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = anoEscolar,
                        onValueChange = { anoEscolar = it },
                        label = "Ano Escolar (opcional)"
                    )

                    CustomTextField(
                        value = escola,
                        onValueChange = { escola = it },
                        label = "Escola (opcional)"
                    )

                    CustomTextField(
                        value = prioridade,
                        onValueChange = { prioridade = it },
                        label = "Prioridade (opcional)"
                    )

                    CustomDropdown(
                        label = "Família",
                        options = familias,
                        selectedOption = selectedFamily,
                        onOptionSelected = { selectedFamily = it }
                    )

                    CustomDropdown(
                        label = "Associação (opcional)",
                        options = associacoes,
                        selectedOption = selectedAssociation,
                        onOptionSelected = { selectedAssociation = it }
                    )

                    Button(
                        onClick = {
                            val addBeneficiary = Beneficiary(
                                nome = nome,
                                nacionalidade = nacionalidade,
                                dataNascimento = dataNascimento,
                                telefone = telefone,
                                numeroIdentificacao = numeroIdentificacao,
                                freguesia = freguesia,
                                cidade = cidade,
                                contadorVisitas = 0,
                                prioridade = prioridade,
                                escola = escola,
                                anoEscolar = anoEscolar
                                // Adicionar data de registo
                            )
                            viewModel.addBeneficiary(addBeneficiary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3851F1)
                        ),
                        enabled = uiState !is BeneficiaryViewModel.UiState.Loading
                    ) {
                        Text("Registar", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
            when (uiState) {
                is BeneficiaryViewModel.UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFF4052F7)
                    )
                }
                is BeneficiaryViewModel.UiState.Success -> {
                    Text(
                        "Beneficiário registrado com sucesso!",
                        color = Color(0xFF4052F7),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                is BeneficiaryViewModel.UiState.Error -> {
                    Text(
                        (uiState as BeneficiaryViewModel.UiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFD9D9D9),
            focusedContainerColor = Color(0xFFD9D9D9),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFF3851F1)
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, "Expand dropdown menu")
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD9D9D9),
                focusedContainerColor = Color(0xFFD9D9D9),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF3851F1)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

    // AppStart coluna "main"
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        // Header
//        TopAppBar(
//            title = { Text("Registar Beneficiário", color = Color.White) },
//            navigationIcon = {
//                IconButton(onClick = onNavigateBack) {
//                    Icon(
//                        Icons.Default.ArrowBack,
//                        contentDescription = "Voltar",
//                        tint = Color.White
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color(0xFF4052F7)
//            )
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Nome field
//            OutlinedTextField(
//                value = nome,
//                onValueChange = { nome = it },
//                label = { Text("Nome") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // Nacionalidade field
//            OutlinedTextField(
//                value = nacionalidade,
//                onValueChange = { nacionalidade = it },
//                label = { Text("Nacionalidade") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // Telefone field
//            OutlinedTextField(
//                value = telefone,
//                onValueChange = { telefone = it },
//                label = { Text("Telefone") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // NumeroIdentificacao field
//            OutlinedTextField(
//                value = numeroIdentificacao,
//                onValueChange = { numeroIdentificacao = it },
//                label = { Text("Numero Identificacao") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // Freguesia field
//            OutlinedTextField(
//                value = freguesia,
//                onValueChange = { freguesia = it },
//                label = { Text("Freguesia") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // Cidade field
//            OutlinedTextField(
//                value = cidade,
//                onValueChange = { cidade = it },
//                label = { Text("Cidade") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // Escola field
//            OutlinedTextField(
//                value = escola,
//                onValueChange = { escola = it },
//                label = { Text("Escola") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            // AnoEscolar field
//            OutlinedTextField(
//                value = anoEscolar,
//                onValueChange = { anoEscolar = it },
//                label = { Text("Ano Escolar") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.LightGray,
//                    focusedBorderColor = Color(0xFF4052F7),
//                    unfocusedContainerColor = Color(0xFFD9D9D9),
//                    focusedContainerColor = Color(0xFFD9D9D9)
//                )
//            )
//
//            Spacer(modifier = Modifier.height(5.dp))
//
//            // Botao final para registar beneficiario
//            Button(
//                onClick = {
//                    viewModel.registerBeneficiary(
//                        nome.trim(),
//                        nacionalidade.trim(),
//                        telefone.trim(),
//                        numeroIdentificacao.trim(),
//                        freguesia.trim(),
//                        cidade.trim(),
//                        contadorVisitas = 0,
//                        prioridade.trim(),
//                        escola.trim(),
//                        anoEscolar.trim()
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF4052F7)
//                ),
//                shape = MaterialTheme.shapes.medium,
//                enabled = uiState !is BeneficiaryRegistrationViewModel.UiState.Loading
//            ) {
//                Text("Registar", style = MaterialTheme.typography.titleLarge)
//            }
//
//            when (uiState) {
//                is BeneficiaryRegistrationViewModel.UiState.Loading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.CenterHorizontally),
//                        color = Color(0xFF4052F7)
//                    )
//                }
//                is BeneficiaryRegistrationViewModel.UiState.Success -> {
//                    Text(
//                        "Beneficiário registrado com sucesso!",
//                        color = Color(0xFF4052F7),
//                        modifier = Modifier.padding(vertical = 16.dp)
//                    )
//                }
//                is BeneficiaryRegistrationViewModel.UiState.Error -> {
//                    Text(
//                        (uiState as BeneficiaryRegistrationViewModel.UiState.Error).message,
//                        color = MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(vertical = 16.dp)
//                    )
//                }
//                else -> {}
//            }
//        }
//    }

