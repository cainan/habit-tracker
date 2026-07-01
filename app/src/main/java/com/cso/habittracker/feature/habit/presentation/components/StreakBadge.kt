package com.cso.habittracker.feature.habit.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cso.habittracker.ui.theme.AppColors
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.TextTertiary

@Composable
fun StreakBadge(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    if (currentStreak > 0) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = AppColors.streak,
                modifier = Modifier.size(11.dp)
            )
            Text(
                text = "$currentStreak day streak",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = AppColors.streak
            )
        }
    } else {
        Text(
            text = "No streak yet",
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = TextTertiary,
            modifier = modifier
        )
    }
}
