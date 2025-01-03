package com.example.lojasocial

import BeneficiaryRegistrationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.presentation.home.HomeScreen
import com.example.lojasocial.ui.theme.LojaSocialTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Initialize Firebase
        setContent {
            LojaSocialTheme {
                val navController = rememberNavController() // Initialize NavController
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    provideDependencies {
                        AppNavHost(navController = navController) // Pass NavController
                    }
                }
            }
        }
    }

    @Composable
    private fun provideDependencies(content: @Composable () -> Unit) {
        val dependencies = (applicationContext as LojaSocialApplication).dependencies
        CompositionLocalProvider(LocalAppDependencies provides dependencies) {
            content()
        }
    }
}

@Composable
fun AppNavHost(navController: androidx.navigation.NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(onMenuItemClick = { menuItem ->
                navController.navigate(menuItem.route) // Navigate using the route
            })
        }
        composable("register") { BeneficiaryRegistrationScreen() }

    }
}
