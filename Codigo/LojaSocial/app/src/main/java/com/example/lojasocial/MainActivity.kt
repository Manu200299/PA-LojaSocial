package com.example.lojasocial

import BeneficiaryRegistrationScreen
import VerifyDonationsScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.presentation.beneficiary.BeneficiaryProfileScreen
import com.example.lojasocial.presentation.beneficiary.CheckInBeneficiaryScreen
import com.example.lojasocial.presentation.beneficiary.CheckOutBeneficiaryScreen
import com.example.lojasocial.presentation.donations.NewDonationScreen
import com.example.lojasocial.presentation.donations.ReceivingDonationsScreen
import com.example.lojasocial.presentation.home.ExitApplicationWithConfirmation
import com.example.lojasocial.presentation.home.HomeScreen
import com.example.lojasocial.presentation.home.LogoutConfirmationScreen
import com.example.lojasocial.presentation.language.LanguageScreen
import com.example.lojasocial.presentation.statistics.StatisticsScreen
import com.example.lojasocial.presentation.stock.StockManagementScreen
import com.example.lojasocial.presentation.stock.InventoryScreen
import com.example.lojasocial.presentation.stock.AddNewItemScreen
import com.example.lojasocial.presentation.visit.VisitStockSelectionScreen
import com.example.lojasocial.presentation.volunteers.LoginVolunteerScreen
import com.example.lojasocial.presentation.volunteers.RegisterVolunteerScreen
import com.example.lojasocial.presentation.volunteers.VolunteerViewModel
import com.example.lojasocial.presentation.volunteers.VolunteersScreen
import com.example.lojasocial.ui.theme.LojaSocialTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FirebaseApp.initializeApp(this) // Initialize Firebase

        setContent {
            LojaSocialTheme {
                val navController = rememberNavController() // Initialize NavController
                val sessionManager = remember { SessionManager(this@MainActivity) }
                var isLoggedIn by remember { mutableStateOf(false) }

                // Observa o fluxo para saber se o user está logado
                LaunchedEffect(Unit) {
                    sessionManager.isLoggedIn.collect { loggedIn ->
                        isLoggedIn = loggedIn
                    }
                }

                // Define as rotas do NavHost
                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "home" else "volunteer_register"
                ) {
                    // HOME
                    composable("home") {
                        HomeScreen(
                            onMenuItemClick = { menuItem ->
                                navController.navigate(menuItem.route)
                            }
                        )
                    }

                    // REGISTAR BENEFICIÁRIO
                    composable("register") {
                        BeneficiaryRegistrationScreen(
                            sessionManager = sessionManager,
                            onNavigateBack = {
                                navController.navigate("home")
                            }
                        )
                    }

                    // CHECK-IN BENEFICIÁRIO
                    composable("check_in") {
                        CheckInBeneficiaryScreen(sessionManager = sessionManager,
                            onBeneficiarySelected = { beneficiary ->
                                navController.navigate("beneficiary_profile/${beneficiary.id}")
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // BENEFICIARY PROFILE
                    composable("beneficiary_profile/{beneficiaryId}", arguments = listOf(navArgument("beneficiaryId") { type = NavType.StringType})) { backStackEntry ->
                        val beneficiaryId = backStackEntry.arguments?.getString("beneficiaryId")
                        BeneficiaryProfileScreen(
                            beneficiaryId = beneficiaryId ?: "",
                            sessionManager = sessionManager,
                            onStartVisitClick = {
                                navController.navigate("visit_stock_selection/$beneficiaryId")
                            },
                            onEditClick = {}
                        )
                    }

                    // VISIT STOCK SELECTION
                    composable(
                        "visit_stock_selection/{beneficiaryId}",
                        arguments = listOf(navArgument("beneficiaryId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val beneficiaryId = backStackEntry.arguments?.getString("beneficiaryId") ?: ""
                        VisitStockSelectionScreen(
                            onNavigateToReview = {
                                navController.navigate("visit_review")
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            sessionManager = sessionManager,
                            beneficiaryId = beneficiaryId
                        )
                    }


                    // CHECK-OUT BENEFICIÁRIO
                    composable("check_out") {
                        CheckOutBeneficiaryScreen()
                    }

                    // GESTÃO DE STOCK
                    composable("stock") {
                        StockManagementScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onInventoryClick = {
                                navController.navigate("inventory_screen")
                            },
                            onAddItemClick = {
                                navController.navigate("add_new_item_screen")
                            }
                        )
                    }

                    // INVENTÁRIO
                    composable("inventory_screen") {
                        // Aqui podes chamar a tua InventoryScreen real.
                        // Coloco aqui um exemplo rápido de placeholder:
                        InventoryScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // ADICIONAR NOVO ITEM AO STOCK
                    composable("add_new_item_screen") {
                        // Chama a tua AddNewItemScreen real
                        AddNewItemScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // DOAÇÕES
                    composable("donations") {
                        ReceivingDonationsScreen(navController = navController)
                    }

                    // NOVA DOAÇÃO
                    composable("new_donation") {
                        NewDonationScreen(
                            onNavigateBack = { navController.popBackStack() },


                            )
                    }


                    // VERIFICAR DOAÇÕES
                    composable("verify_donations") {
                        VerifyDonationsScreen(
                            onNavigateBack = { navController.popBackStack() },

                            )
                    }

                    // ESTATÍSTICAS
//                    composable("statistics") {
//                        val beneficiaryRepository = BeneficiaryRepositoryImpl() // Ajeita isso Manel
//                        StatisticsScreen(
//                            onNavigateBack = { navController.popBackStack() },
//                            beneficiaryRepository = beneficiaryRepository
//                        )
//                    }

                    composable("volunteers") {
                        VolunteersScreen(
                            sessionManager = sessionManager,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }




                    // IDIOMA
                    composable("language_screen") {
                        LanguageScreen(onNavigateBack = { navController.popBackStack() })
                    }



                    // REGISTAR VOLUNTÁRIO
                    composable("volunteer_register") {
                        RegisterVolunteerScreen(
                            sessionManager = sessionManager,
                            onBackToLogin = {
                                navController.navigate("volunteer_login")
                            }
                        )
                    }

                    // LOGIN VOLUNTÁRIO
                    composable("volunteer_login") {
                        LoginVolunteerScreen(sessionManager = sessionManager)
                    }

                    composable("logout") {
                        LogoutConfirmationScreen(navController, sessionManager)
                    }

                    // SAIR (com confirmação)
                    composable("exit") {
                        ExitApplicationWithConfirmation(navController)
                    }
                }
            }
        }
    }
}