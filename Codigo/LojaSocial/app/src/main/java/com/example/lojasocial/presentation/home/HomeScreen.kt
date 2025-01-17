package com.example.lojasocial.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuItemClick: (MenuItem) -> Unit,
    sessionManager: SessionManager
) {
    // Coleta o nome do voluntário logado
    val volunteerNameFlow = sessionManager.nome
    // Transforma o Flow<String?> em um State<String> seguro
    val volunteerName by volunteerNameFlow.collectAsState(initial = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(

            title = { Text("Home", color = Color.White) },
            // Adiciona ações no canto superior direito
            actions = {
                Text(
                    text = volunteerName ?: "",     // Exibe o nome do voluntário
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Volunteer Icon",
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(16.dp))

            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3851F1))
        )

        // Imagem do Logo
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_sao_lazaro ), // <-- Ajuste o nome do seu drawable
            contentDescription = "Loja Social São Lázaro e São João do Souto Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),  // Ajuste conforme desejar
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Grid Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MenuItem.values()) { menuItem ->
                    MenuCard(
                        menuItem = menuItem,
                        onClick = { onMenuItemClick(menuItem) },
                        size = 100.dp
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9))
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
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = menuItem.title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
