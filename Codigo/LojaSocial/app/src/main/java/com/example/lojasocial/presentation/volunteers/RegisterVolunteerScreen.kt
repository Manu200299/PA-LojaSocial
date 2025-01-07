package com.example.lojasocial.presentation.volunteers

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberAsyncImagePainter
import com.example.lojasocial.R
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.worker.LogoDownloadWorker
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVolunteerScreen(
    onBackToLogin: () -> Unit,
    onNavigateBack: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    sessionManager: SessionManager
) {
    val viewModel: VolunteerViewModel = viewModel(
        factory = VolunteerViewModel.Factory(sessionManager)
    )

    var volunteerId by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var logoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "loja_social_logo.png")
        if (file.exists()) {
            logoUri = Uri.fromFile(file)
        } else {
            val workManager = WorkManager.getInstance(context)
            val logoDownloadRequest = OneTimeWorkRequestBuilder<LogoDownloadWorker>()
                .setInputData(
                    workDataOf(
                        LogoDownloadWorker.KEY_LOGO_URL to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFtL-B4gbbMxU-xnCG-BU5FF0SS0-4RmjgzA&s"
                    )
                )
                .build()

            workManager.enqueue(logoDownloadRequest)

            workManager.getWorkInfoByIdLiveData(logoDownloadRequest.id)
                .observeForever { workInfo ->
                    if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                        logoUri = Uri.fromFile(file)
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Voluntário", color = Color.White) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //Logo
            logoUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Loja Social Logo",
                    modifier = Modifier
                        .size(240.dp)
                        .padding(vertical = 32.dp)
                )
            }

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = {
                    val addVolunteer = Volunteer(
                        nome = nome,
                        telefone = telefone,
                        password = password,
                    )
                    viewModel.registerVolunteer(addVolunteer)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is VolunteerViewModel.UiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1))
            ) {
                Text("Registrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBackToLogin
            ) {
                Text("Already have an account? Click here to login")
            }


            when (uiState) {
                is VolunteerViewModel.UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is VolunteerViewModel.UiState.Success -> {
                    LaunchedEffect(Unit) {
                        onRegisterSuccess()
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

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {

//
//        // Campos preenchimento
//        OutlinedTextField(
//            value = nome,
//            onValueChange = { nome = it },
//            label = { Text("Nome") },
//            modifier = Modifier.fillMaxWidth(),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedContainerColor = Color(0xFFD9D9D9),
//                focusedContainerColor = Color(0xFFD9D9D9),
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = Color(0xFF3851F1)
//            ),
//            shape = RoundedCornerShape(8.dp),
//            singleLine = true
//        )
//
//        OutlinedTextField(
//            value = telefone,
//            onValueChange = { telefone = it },
//            label = { Text("Número Telefone") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//            modifier = Modifier.fillMaxWidth(),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedContainerColor = Color(0xFFD9D9D9),
//                focusedContainerColor = Color(0xFFD9D9D9),
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = Color(0xFF3851F1)
//            ),
//            shape = RoundedCornerShape(8.dp),
//            singleLine = true
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Palavra-Chave") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth(),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedContainerColor = Color(0xFFD9D9D9),
//                focusedContainerColor = Color(0xFFD9D9D9),
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = Color(0xFF3851F1)
//            ),
//            shape = RoundedCornerShape(8.dp),
//            singleLine = true
//        )
//
//        OutlinedTextField(
//            value = confirmPassword,
//            onValueChange = { password = it},
//            label = { Text("Confirmar Palavra-Chave")},
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth(),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedContainerColor = Color(0xFFD9D9D9),
//                focusedContainerColor = Color(0xFFD9D9D9),
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = Color(0xFF3851F1)
//            ),
//            shape = RoundedCornerShape(8.dp),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Botao Registar
//        Button(
//            onClick = {
//                val addVolunteer = Volunteer(
//                    nome = nome,
//                    telefone = telefone,
//                    password = password,
//                )
//                viewModel.registerVolunteer(addVolunteer)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            shape = RoundedCornerShape(28.dp),
//            enabled = uiState !is VolunteerViewModel.UiState.Loading,
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3851F1))
//        ) {
//            Text("Registrar",
//                style = MaterialTheme.typography.titleLarge,
//                color = Color.White)
//        }
//    }
}


