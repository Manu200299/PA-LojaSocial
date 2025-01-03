package com.example.lojasocial.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun ExitApplicationWithConfirmation(navController: NavController) {
    val context = LocalContext.current // Access context inside a Composable scope

    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text("Exit Application") },
        text = { Text("Are you sure you want to exit?") },
        confirmButton = {
            Button(
                onClick = {
                    (context as? ComponentActivity)?.finishAndRemoveTask()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue) // Blue button color
            ) {
                Text("Yes", color = Color.White) // Text color white for contrast
            }
        },
        dismissButton = {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue) // Blue button color
            ) {
                Text("No", color = Color.White) // Text color white for contrast
            }
        }
    )
}
