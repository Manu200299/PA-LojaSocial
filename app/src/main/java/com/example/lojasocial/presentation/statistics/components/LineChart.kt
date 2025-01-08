package com.example.lojasocial.presentation.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(data: List<Pair<String, Int>>) {
    if (data.isEmpty()) return

    // Determinar o valor máximo para normalizar os dados
    val maxCount = data.maxOf { it.second }

    // Mapear os pontos no gráfico
    val points = data.mapIndexed { index, stat ->
        Offset(
            x = index * 100f,
            y = 150f - (stat.second.toFloat() / maxCount * 150f) // Usar o valor normalizado
        )
    }

    // Desenhar o gráfico
    Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {

        for (i in 0 until points.size - 1) {
            drawLine(
                color = Color(0xFF3851F1),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 5f
            )
        }

        // Desenhar os pontos nos vértices
        points.forEach { point ->
            drawCircle(
                color = Color(0xFF3851F1),
                radius = 8f,
                center = point
            )
        }
    }
}
