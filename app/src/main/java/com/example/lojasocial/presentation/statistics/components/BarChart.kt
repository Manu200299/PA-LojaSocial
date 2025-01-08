package com.example.lojasocial.presentation.statistics.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun BarChart(data: List<com.example.lojasocial.presentation.statistics.components.NationalityStats>) {
    val maxCount = data.maxOf { it.count }
    val barWidth = 30.dp

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        data.forEach { stat ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height((stat.count.toFloat() / maxCount) * 150.dp)
                        .background(Color(0xFF3851F1))
                )
                Text(stat.country, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
