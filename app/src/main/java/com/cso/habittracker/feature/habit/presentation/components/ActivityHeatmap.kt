package com.cso.habittracker.feature.habit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cso.habittracker.ui.theme.Border
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.Primary
import com.cso.habittracker.ui.theme.Secondary
import com.cso.habittracker.ui.theme.SurfaceElevated
import com.cso.habittracker.ui.theme.TextTertiary
import java.time.DayOfWeek
import java.time.LocalDate

private val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

private fun completionColor(ratio: Float): Color = when {
    ratio <= 0f -> SurfaceElevated
    ratio < 0.34f -> Primary.copy(alpha = 0.30f)
    ratio < 0.67f -> Primary.copy(alpha = 0.55f)
    ratio < 1f -> Primary.copy(alpha = 0.80f)
    else -> Primary
}

/**
 * 7-column × 4-row activity heatmap.
 *
 * @param heatmapData Map of date → completion ratio (0f–1f). Days not in the map are future cells.
 * @param startDate The Monday starting the first row (must be a Monday, 3 weeks before current week).
 * @param today Used to distinguish past/today cells from future ones.
 */
@Composable
fun ActivityHeatmap(
    heatmapData: Map<LocalDate, Float>,
    startDate: LocalDate,
    today: LocalDate,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Day-of-week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            dayLabels.forEach { label ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        // 4-week grid
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(4) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(7) { dayIndex ->
                        val date = startDate.plusDays((week * 7 + dayIndex).toLong())
                        val isFuture = date.isAfter(today)
                        val isToday = date == today
                        val ratio = heatmapData[date] ?: 0f

                        val cellColor = if (isFuture) Border.copy(alpha = 0.3f) else completionColor(ratio)
                        val borderMod = if (isToday) {
                            Modifier.border(2.dp, Secondary, RoundedCornerShape(6.dp))
                        } else {
                            Modifier
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(cellColor)
                                .then(borderMod)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Less",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.width(6.dp))
            listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { ratio ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .width(14.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(completionColor(ratio))
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "More",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = TextTertiary
            )
        }
    }
}
