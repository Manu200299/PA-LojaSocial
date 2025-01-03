package com.example.lojasocial.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    REGISTER("Registar Beneficiário\nou Família", Icons.Outlined.Person, "register"),
    CHECK_IN("Check-In Beneficiário", Icons.Outlined.CheckCircle, "check_in"),
    CHECK_OUT("Check-Out Beneficiário", Icons.Outlined.ExitToApp, "check_out"),
    STOCK("Gestão de Stock", Icons.Outlined.Person, "stock"),
    DONATIONS("Receber Doações", Icons.Outlined.Person, "donations"),
    VOLUNTEERS("Gestão Voluntários", Icons.Outlined.Person, "volunteers"),
    STATISTICS("Dados Estatísticos", Icons.Outlined.Person, "statistics"),
    LANGUAGE("Alterar Idioma", Icons.Outlined.Person, "language"),
    EXIT("Sair", Icons.Outlined.Close, "exit")
}
