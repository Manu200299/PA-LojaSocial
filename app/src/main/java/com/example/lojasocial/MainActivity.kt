package com.example.lojasocial

import BeneficiaryRegistrationScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.data.repository.VolunteerRepositoryImpl
import com.example.lojasocial.presentation.Stock.StockManagementScreen
import com.example.lojasocial.presentation.beneficiary.BeneficiaryProfileScreen
import com.example.lojasocial.presentation.beneficiary.CheckInBeneficiaryScreen
import com.example.lojasocial.presentation.beneficiary.CheckOutBeneficiaryScreen
import com.example.lojasocial.presentation.donations.NewDonationScreen
import com.example.lojasocial.presentation.donations.ReceivingDonationsScreen
import com.example.lojasocial.presentation.donations.VerifyDonationsScreen
import com.example.lojasocial.presentation.home.ExitApplicationWithConfirmation
import com.example.lojasocial.presentation.home.HomeScreen
import com.example.lojasocial.presentation.language.LanguageScreen
import com.example.lojasocial.presentation.statistics.StatisticsDataScreen
import com.example.lojasocial.presentation.volunteers.LoginVolunteerScreen
import com.example.lojasocial.presentation.volunteers.RegisterVolunteerScreen
import com.example.lojasocial.presentation.volunteers.VolunteersScreen
import com.example.lojasocial.ui.theme.LojaSocialTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Initialize Firebase

        setContent {
            LojaSocialTheme {
                val navController = rememberNavController() // Initialize NavController
                val sessionManager = remember { SessionManager(this@MainActivity) }
                var isLoggedIn by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    sessionManager.isLoggedIn.collect { loggedIn ->
                        isLoggedIn = loggedIn
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "home" else "volunteer_login"
                ) {
                    composable("home") {
                        HomeScreen(onMenuItemClick = { menuItem ->
                            navController.navigate(menuItem.route) // Navigate using the route
                        })
                    }
                    composable("register") {
                        BeneficiaryRegistrationScreen(
                            sessionManager = sessionManager,
                            onNavigateBack = {
                                navController.navigate("home")
                            })
                    }

                    composable("check_in") { CheckInBeneficiaryScreen(
                        sessionManager = sessionManager,
                        onBeneficiarySelected = { beneficiary ->
                            navController.navigate("beneficiary_profile/${beneficiary.id}")
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    ) }

                    composable("beneficiary_profile/{beneficiaryId}", arguments = listOf(navArgument("beneficiaryId") { type = NavType.StringType})
                    ){ backStackEntry ->
                        val beneficiaryId = backStackEntry.arguments?.getString("beneficiaryId")
                        BeneficiaryProfileScreen(
                            beneficiaryId = beneficiaryId ?: "",
                            sessionManager = sessionManager
                        )
                    } // beneficiary profile screen

                    composable("check_out") { CheckOutBeneficiaryScreen() }

                    composable("stock") { StockManagementScreen() }

                    composable("donations") {
                        ReceivingDonationsScreen(navController = navController)
                    }

                    composable("new_donation") {
                        NewDonationScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onAddItem = { donationItem ->
                                println("New donation added: $donationItem")
                                navController.popBackStack() // Return to ReceivingDonationsScreen
                            }
                        )
                    }

                    composable("verify_donations") {
                        VerifyDonationsScreen(
                            onNavigateBack = { navController.popBackStack() },
//                            donations = TODO()
                        )
                    }

                    composable("volunteers") { VolunteersScreen() }

                    composable("statistics") { StatisticsDataScreen() }

                    composable("language") { LanguageScreen() }

                    composable("volunteer_register") {
                        RegisterVolunteerScreen(
                            sessionManager = sessionManager,
                            onBackToLogin = {
                                navController.navigate("volunteer_login")
                            })
                    }

                    composable("volunteer_login") { LoginVolunteerScreen(sessionManager = sessionManager) }

                    composable("exit") {
                        ExitApplicationWithConfirmation(navController)
                    }
                }
            }
        }
    }
}
