package com.example.lojasocial.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateBack: () -> Unit = {},
    onMenuItemClick: (MenuItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Home", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF3851F1)
            )
        )

        // Grid Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val numberOfColumns = 3
            val numberOfRows = 3 // Adjust for the number of menu items
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp - 64.dp // Adjust for TopAppBar height
            val itemSpacing = 8.dp
            val itemWidth = (screenWidth - (itemSpacing * (numberOfColumns + 1))) / numberOfColumns
            val itemHeight = (screenHeight - (itemSpacing * (numberOfRows + 1))) / numberOfRows

            LazyVerticalGrid(
                columns = GridCells.Fixed(numberOfColumns),
                contentPadding = PaddingValues(itemSpacing),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                verticalArrangement = Arrangement.spacedBy(itemSpacing),
                modifier = Modifier.fillMaxSize()
            ) {
                items(MenuItem.values()) { menuItem ->
                    MenuCard(
                        menuItem = menuItem,
                        onClick = { onMenuItemClick(menuItem) },
                        size = itemWidth.coerceAtMost(itemHeight)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    menuItem: MenuItem,
    onClick: () -> Unit,
    size: Dp
) {
    Card(
        modifier = Modifier
            .size(size)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD9D9D9)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF292D32)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = menuItem.title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF000000)
            )
        }
    }
}

enum class MenuItem(
    val title: String,
    val icon: ImageVector
) {
    REGISTER("Registar Beneficiário\nou Família", Icons.Outlined.Person),
    CHECK_IN("Check-In Beneficiário", Icons.Outlined.CheckCircle),
    CHECK_OUT("Check-Out Beneficiário", Icons.Outlined.ExitToApp),
    STOCK("Gestão de Stock", Icons.Outlined.Person),
    DONATIONS("Receber Doações", Icons.Outlined.Person),
    VOLUNTEERS("Gestão Voluntários", Icons.Outlined.Person),
    STATISTICS("Dados Estatísticos", Icons.Outlined.Person),
    LANGUAGE("Alterar Idioma", Icons.Outlined.Person),
    EXIT("Sair", Icons.Outlined.Close)
}