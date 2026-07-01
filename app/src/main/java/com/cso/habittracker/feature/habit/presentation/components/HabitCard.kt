package com.cso.habittracker.feature.habit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cso.habittracker.feature.habit.domain.model.Habit
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.Surface
import com.cso.habittracker.ui.theme.TextPrimary

@Composable
fun HabitCard(
    habit: Habit,
    isCompleted: Boolean,
    onCardClick: () -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .clickable(onClick = onCardClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HabitIconBox(
            icon = habit.icon,
            containerSize = 42.dp,
            iconSize = 22.dp
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = habit.name,
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(3.dp))
            StreakBadge(currentStreak = habit.currentStreak)
        }
        CompletionToggle(
            isCompleted = isCompleted,
            onToggle = onToggle
        )
    }
}
