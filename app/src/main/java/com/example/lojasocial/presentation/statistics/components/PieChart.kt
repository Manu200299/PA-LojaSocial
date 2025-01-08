package com.example.lojasocial.presentation.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.example.lojasocial.domain.repository.StatisticsRepository

@Composable
fun PieChart(data: List<StatisticsRepository.ItemStats>) {
    val total = data.fold(0f) { acc, item -> acc + item.percentage }
    val percentages = data.map { it.percentage / total * 360 }

    Canvas(modifier = Modifier.size(200.dp)) {
        var startAngle = 0f
        percentages.forEachIndexed { index, sweepAngle ->
            drawArc(
                color = listOf(Color.Blue, Color.Magenta, Color.Cyan)[index % 3],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                style = Fill
            )
            startAngle += sweepAngle
        }
    }
}
