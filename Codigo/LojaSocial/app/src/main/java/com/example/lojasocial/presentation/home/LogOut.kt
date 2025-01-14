package com.example.lojasocial.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.presentation.volunteers.VolunteerViewModel

@Composable
fun LogoutConfirmationScreen(
    navController: NavController,
    sessionManager: SessionManager,
) {

    val viewModel: VolunteerViewModel = viewModel(
        factory = VolunteerViewModel.Factory(sessionManager)
    )

    // Se quiseres que o botão "Back" também cancele, podes interceptar aqui
    BackHandler {
        navController.popBackStack()
    }

    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text("Terminar Sessão") },
        text = { Text("Tem a certeza que pretende terminar a sessão?") },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.logout()  // Chama o logout
                    // Após o logout, vais para o ecrã de login (por exemplo)
                    navController.navigate("volunteer_login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Sim", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Não", color = Color.White)
            }
        }
    )
}
