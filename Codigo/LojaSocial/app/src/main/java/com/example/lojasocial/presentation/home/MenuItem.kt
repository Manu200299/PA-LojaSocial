package com.example.lojasocial.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    REGISTER("Registar Beneficiário", Icons.Outlined.Person, "register"),
    CHECK_IN("Check-In Beneficiário", Icons.Outlined.CheckCircle, "check_in"),
    CHECK_OUT("Check-Out Beneficiário", Icons.Outlined.ExitToApp, "check_out"),
    STOCK("Gestão de Stock", Icons.Outlined.AddBox, "stock"),
    DONATIONS("Receber Doações", Icons.Outlined.BookmarkAdd, "donations"),
    VOLUNTEERS("Gestão Voluntários", Icons.Outlined.Accessibility, "volunteers"),
    STATISTICS("Dados Estatísticos", Icons.Outlined.BarChart, "statistics"),
    LANGUAGE("Alterar Idioma", Icons.Outlined.Language, "language"),
    LOGIN_VOLUNTEER("Login Voluntário", Icons.Outlined.Login, "volunteer_login"), //
    REGISTER_VOLUNTEER("Registrar Voluntário", Icons.Outlined.AppRegistration, "volunteer_register"),
    Logout("Log-out", Icons.Outlined.Logout, "logout"),
    EXIT("Sair", Icons.Outlined.Close, "exit")
}
