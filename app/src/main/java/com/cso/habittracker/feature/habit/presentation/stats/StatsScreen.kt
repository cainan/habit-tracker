package com.cso.habittracker.feature.habit.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cso.habittracker.feature.habit.domain.model.Habit
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import com.cso.habittracker.feature.habit.presentation.components.ActivityHeatmap
import com.cso.habittracker.feature.habit.presentation.components.HabitIconBox
import com.cso.habittracker.ui.components.AppTopBar
import com.cso.habittracker.ui.components.ObserveAsEvents
import com.cso.habittracker.ui.components.SectionTitle
import com.cso.habittracker.ui.theme.AppColors
import com.cso.habittracker.ui.theme.Background
import com.cso.habittracker.ui.theme.HabitTrackerTheme
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.Primary
import com.cso.habittracker.ui.theme.Secondary
import com.cso.habittracker.ui.theme.Streak
import com.cso.habittracker.ui.theme.Surface
import com.cso.habittracker.ui.theme.TextPrimary
import com.cso.habittracker.ui.theme.TextSecondary
import com.cso.habittracker.ui.theme.TextTertiary
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun StatsRoot(
    onNavigateBack: () -> Unit,
    viewModel: StatsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            StatsEvent.NavigateBack -> onNavigateBack()
        }
    }

    StatsScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun StatsScreen(
    state: StatsState,
    onAction: (StatsAction) -> Unit
) {
    Scaffold(containerColor = Background) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                AppTopBar(
                    title = "Statistics",
                    onBack = { onAction(StatsAction.NavigateBack) }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        label = "This Week",
                        value = "${state.thisWeekPct}%",
                        valueColor = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        label = "Best Streak",
                        value = "${state.bestStreak}d",
                        valueColor = Streak,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        label = "Active",
                        value = "${state.activeCount}",
                        valueColor = Secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Column {
                    SectionTitle(text = "Activity")
                    Spacer(modifier = Modifier.height(12.dp))
                    ActivityHeatmap(
                        heatmapData = state.heatmapData,
                        startDate = state.heatmapStartDate,
                        today = state.today
                    )
                }
            }

            if (state.streakRows.isNotEmpty()) {
                item {
                    SectionTitle(text = "Streaks")
                }
                items(state.streakRows, key = { it.habit.id }) { row ->
                    StreakRow(row = row)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .padding(14.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = value,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            color = TextTertiary
        )
    }
}

@Composable
private fun StreakRow(
    row: HabitStreakRow,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HabitIconBox(
            icon = row.habit.icon,
            containerSize = 36.dp,
            iconSize = 18.dp,
            cornerRadius = 10.dp
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = row.habit.name,
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Text(
                text = "Best: ${row.bestStreak} days",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = TextTertiary
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${row.currentStreak}",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = AppColors.streak
            )
            Text(
                text = "day streak",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = TextTertiary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun StatsScreenPreview() {
    val habit = Habit(
        id = 1L,
        name = "Morning Run",
        icon = HabitIcon.RUN,
        scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
        currentStreak = 12,
        bestStreak = 21
    )
    val today = LocalDate.now()
    HabitTrackerTheme {
        StatsScreen(
            state = StatsState(
                thisWeekPct = 75,
                bestStreak = 21,
                activeCount = 5,
                heatmapData = emptyMap(),
                streakRows = listOf(
                    HabitStreakRow(habit, 12, 21),
                    HabitStreakRow(habit.copy(id = 2L, name = "Read 30min", icon = HabitIcon.READ, currentStreak = 7, bestStreak = 14), 7, 14)
                ),
                heatmapStartDate = today.minusWeeks(3),
                today = today,
                isLoading = false
            ),
            onAction = {}
        )
    }
}
