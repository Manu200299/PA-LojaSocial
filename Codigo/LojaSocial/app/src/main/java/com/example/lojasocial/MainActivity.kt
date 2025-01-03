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
import com.example.lojasocial.presentation.beneficiary.CheckInBeneficiaryScreen
import com.example.lojasocial.ui.theme.LojaSocialTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            LojaSocialTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    provideDependencies {
                        CheckInBeneficiaryScreen()
//                        BeneficiaryRegistrationScreen()
//                        HomeScreen(
//                            onNavigateBack = { /* Mock action for back navigation */ },
//                            onMenuItemClick = { menuItem ->
//                                // Mock action for menu item clicks
//                                println("Clicked on menu item: ${menuItem.title}")
//                            }
//                        )
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
