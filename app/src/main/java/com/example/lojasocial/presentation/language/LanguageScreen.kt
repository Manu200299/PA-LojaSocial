package com.example.lojasocial.presentation.language

fun LanguageScreen() {

}
/*
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
 import com.yariksoffice.lingver.Lingver
import com.example.lojasocial.MainActivity

@Composable
fun LanguageScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Selecione o idioma:")

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para Português
        Button(onClick = {
            Lingver.getInstance().setLocale(context, "pt", "PT")
            restartApp(context)
        }) {
            Text(text = "Português")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para Inglês
        Button(onClick = {
            Lingver.getInstance().setLocale(context, "en", "US")
            restartApp(context)
        }) {
            Text(text = "Inglês")
        }
    }
}

private fun restartApp(context: android.content.Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}
*/