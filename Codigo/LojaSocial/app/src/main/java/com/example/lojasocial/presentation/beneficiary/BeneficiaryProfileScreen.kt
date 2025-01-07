package com.example.lojasocial.presentation.beneficiary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.model.BeneficiaryDto
import com.example.lojasocial.domain.model.Beneficiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryProfileScreen(
    sessionManager: SessionManager,
    beneficiaryId: String,
    viewModel: BeneficiaryViewModel = viewModel(
        factory = BeneficiaryViewModel.Factory(sessionManager)
    ),
    onNavigateBack: () -> Unit = {},
    onEditClick: (Beneficiary) -> Unit = {},
    onStartVisitClick: (Beneficiary) -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsState()
    val beneficiary by viewModel.selectedBeneficiary.collectAsState()

    LaunchedEffect(beneficiaryId) {
        viewModel.getBeneficiarybyId(beneficiaryId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Perfil do Beneficiario",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            },
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
                containerColor = Color(0xFF3851F1)
            )
        )

        when (uiState) {
            is BeneficiaryViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF3851F1))
                }
            }

            is BeneficiaryViewModel.UiState.Success -> {
                beneficiary?.let { beneficiaryData ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF3851F1),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .background(
                                                    color = Color(0xFFEADDFF),
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                modifier = Modifier.size(40.dp),
                                                tint = Color(0xFF4F378A)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = beneficiaryData.nome,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.Black
                                            )

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarToday,
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = "Visitas: ${beneficiaryData.contadorVisitas}",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFFC300),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = "Prioridade: (${beneficiaryData.prioridade})",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = Color(0xFFFFC300)
                                                )
                                            }
                                        }
                                    }

                                    Button(
                                        onClick = { onEditClick(beneficiaryData) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF3851F1)
                                        ),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        Text("Editar Perfil")
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tag,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )
                                        Text(
                                            text = "Nº ID / Passaporte: ${beneficiaryData.numeroIdentificacao}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )
                                        Text(
                                            text = "Número Telefone: ${beneficiaryData.telefone}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.People,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )
                                        Text(
                                            // text = "Agregado Familiar: ${beneficiaryData.agregadoFamiliar}",
                                            text = "Agregado Familiar: POR IMPLEMENTAR",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Descrição Família:",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        TextField(
                                            // value = beneficiaryData.descricaoFamilia ?: "",
                                            value = "POR IMPLEMENTAR",
                                            onValueChange = { },
                                            readOnly = true,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp),
                                            colors = TextFieldDefaults.colors(
                                                unfocusedContainerColor = Color(0xFFD9D9D9),
                                                focusedContainerColor = Color(0xFFD9D9D9),
                                                disabledContainerColor = Color(0xFFD9D9D9),
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { onStartVisitClick(beneficiaryData) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3851F1)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                "Iniciar Visita",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            is BeneficiaryViewModel.UiState.Error -> {
                Text(
                    "Erro: ${(uiState as BeneficiaryViewModel.UiState.Error).message}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFF44336),
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                // Idle state
            }
        }
    }
}
//                            }
//                        }
//                        BeneficiaryInfoCard(beneficiaryData)
//
//                        Button(
//                            onClick = { onStartVisitClick(beneficiaryData) },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(56.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF3851F1)
//                            ),
//                        shape = RoundedCornerShape(28.dp)
//                        ) {
//                            Text("Iniciar Visita", style = MaterialTheme.typography.titleLarge)
//                        }
//                    }
//                }
//            }
//            is BeneficiaryViewModel.UiState.Error -> {
//                Text(
//                    "Erro: ${(uiState as BeneficiaryViewModel.UiState.Error).message}",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color(0xFFF44336),
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//            else -> {
//
//            }
//        }
//    }
//}
//
//@Composable
//fun BeneficiaryInfoCard(beneficiary: Beneficiary){
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = beneficiary.nome,
//                style = MaterialTheme.typography.headlineMedium
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            BeneficiaryInfoItem("ID", beneficiary.id)
//            BeneficiaryInfoItem("Telefone", beneficiary.telefone)
//            BeneficiaryInfoItem("Nº Identificação", beneficiary.numeroIdentificacao)
//            BeneficiaryInfoItem("Nacionalidade", beneficiary.nacionalidade)
//            BeneficiaryInfoItem("Data de Nascimento", beneficiary.dataNascimento)
//            BeneficiaryInfoItem("Freguesia", beneficiary.freguesia)
//            BeneficiaryInfoItem("Cidade", beneficiary.cidade)
//            BeneficiaryInfoItem("Prioridade", beneficiary.prioridade)
//            BeneficiaryInfoItem("Escola", beneficiary.escola)
//            BeneficiaryInfoItem("Ano Escolar", beneficiary.anoEscolar)
//            BeneficiaryInfoItem("Número de Visitas", beneficiary.contadorVisitas.toString())
//        }
//    }
//}
//
//@Composable
//fun BeneficiaryInfoItem(label: String, value: String){
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ){
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodyLarge,
//            color = Color.Gray
//        )
//        Text(
//            text = value,
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        // Top Bar
//        TopAppBar(
//            title = { Text("Perfil Beneficiário", color = Color.White) },
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
//                containerColor = Color(0xFF3851F1)
//            )
//        )
//
//        // Content
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Profile Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                border = CardDefaults.outlinedCardBorder()
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.Top
//                ) {
//                    // Left section with avatar and name
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(8.dp)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(80.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFFEADDFF))
//                                .padding(16.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Person,
//                                contentDescription = null,
//                                modifier = Modifier.size(48.dp),
//                                tint = Color(0xFF4F378B)
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            beneficiaryDto.nome,
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    }
//
//                    // Right section with stats and edit button
//                    Column(
//                        modifier = Modifier.weight(1f),
//                        horizontalAlignment = Alignment.End
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.DateRange,
//                                contentDescription = null,
//                                tint = Color(0xFF3851F1)
//                            )
//                            Text(
//                                "Visitas: ${beneficiaryDto.contadorVisitas}",
//                                style = MaterialTheme.typography.titleMedium,
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        ) {
//                            Text(
//                                "Prioridade: ",
//                                style = MaterialTheme.typography.titleMedium
//                            )
//                            Text(
//                                beneficiaryDto.prioridade,
//                                color = Color(0xFFFFC300),
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Warning,
//                                contentDescription = null,
//                                tint = Color(0xFFFF0000)
//                            )
//                            Text(
//                                "Lista: ${beneficiaryDto.prioridade}",
//                                style = MaterialTheme.typography.titleMedium,
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                        Button(
//                            onClick = onEditClick,
//                            modifier = Modifier.padding(top = 8.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF3851F1)
//                            )
//                        ) {
//                            Text("Editar Perfil")
//                        }
//                    }
//                }
//            }
//
//            // Details Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                border = CardDefaults.outlinedCardBorder()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Row {
//                        Text(
//                            "Nº ID / Passaporte: ",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        Text(beneficiaryDto.numeroIdentificacao)
//                    }
//                    Row {
//                        Text(
//                            "Número Telefone: ",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        Text(beneficiaryDto.telefone)
//                    }
//                    Row {
//                        Text(
//                            "Agregado Familiar: ",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        Text(beneficiaryDto.nome)
//                    }
//                    Column {
//                        Text(
//                            "Descrição Familia:",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        OutlinedTextField(
//                            value = "",
//                            onValueChange = {},
//                            modifier = Modifier.fillMaxWidth(),
//                            readOnly = true,
//                            colors = OutlinedTextFieldDefaults.colors(
//                                unfocusedContainerColor = Color(0xFFD9D9D9),
//                                focusedContainerColor = Color(0xFFD9D9D9)
//                            )
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Start Visit Button
//            Button(
//                onClick = onStartVisitClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF3851F1)
//                ),
//                shape = RoundedCornerShape(28.dp)
//            ) {
//                Text(
//                    "Iniciar Visita",
//                    style = MaterialTheme.typography.titleLarge
//                )
//            }
//        }
//    }
//}

