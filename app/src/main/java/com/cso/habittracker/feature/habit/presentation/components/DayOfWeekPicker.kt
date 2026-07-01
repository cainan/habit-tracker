package com.cso.habittracker.feature.habit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cso.habittracker.ui.theme.AppColors
import com.cso.habittracker.ui.theme.Border
import com.cso.habittracker.ui.theme.Destructive
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.Primary
import com.cso.habittracker.ui.theme.Secondary
import com.cso.habittracker.ui.theme.Surface
import com.cso.habittracker.ui.theme.SurfaceBright
import com.cso.habittracker.ui.theme.TextTertiary
import java.time.DayOfWeek

private val orderedDays = listOf(
    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
)
private val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun DayOfWeekPicker(
    selectedDays: Set<DayOfWeek>,
    onToggle: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
    hasError: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        orderedDays.forEachIndexed { index, day ->
            val isSelected = day in selectedDays
            val shape = RoundedCornerShape(10.dp)
            val bgColor = if (isSelected) SurfaceBright else Surface
            val borderColor = when {
                hasError -> Destructive
                isSelected -> Primary
                else -> Border
            }
            val textColor = if (isSelected) Secondary else TextTertiary

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(shape)
                    .background(bgColor)
                    .border(if (isSelected) 1.5.dp else 1.dp, borderColor, shape)
                    .clickable { onToggle(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayLabels[index],
                    fontFamily = InterFamily,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 13.sp,
                    color = textColor
                )
            }
        }
    }
}
