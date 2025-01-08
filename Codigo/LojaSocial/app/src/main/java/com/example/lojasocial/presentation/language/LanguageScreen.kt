package com.example.lojasocial.presentation.language

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lojasocial.R
import com.yariksoffice.lingver.Lingver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Lista de idiomas e seus respectivos códigos
    val languages = listOf(
        stringResource(R.string.language_portuguese),
        stringResource(R.string.language_english),
        stringResource(R.string.language_french),
        stringResource(R.string.language_spanish),
        stringResource(R.string.language_ukrainian)
    )
    val languageCodes = listOf("pt", "en", "fr", "es", "uk")

    // Idioma atualmente selecionado (obtido do Lingver)
    var selectedLanguage by remember { mutableStateOf(Lingver.getInstance().getLanguage()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_change_language), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3851F1))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            languages.forEachIndexed { index, language ->
                val isSelected = selectedLanguage == languageCodes[index]

                LanguageItem(
                    language = language,
                    isSelected = isSelected,
                    onSelect = {
                        Lingver.getInstance().setLocale(context, languageCodes[index])
                        selectedLanguage = languageCodes[index]
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = language,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.Gray else Color.Black
        )

        Button(
            onClick = onSelect,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color.Gray else Color(0xFF3851F1),
                contentColor = Color.White
            ),
            enabled = !isSelected
        ) {
            Text(text = if (isSelected) stringResource(R.string.button_selected) else stringResource(R.string.button_select))
        }
    }
}
