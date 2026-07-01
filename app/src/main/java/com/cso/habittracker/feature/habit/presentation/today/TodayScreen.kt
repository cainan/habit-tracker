package com.cso.habittracker.feature.habit.presentation.today

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.cso.habittracker.feature.habit.presentation.components.HabitCard
import com.cso.habittracker.ui.components.ObserveAsEvents
import com.cso.habittracker.ui.theme.Background
import com.cso.habittracker.ui.theme.HabitTrackerTheme
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.Primary
import com.cso.habittracker.ui.theme.Secondary
import com.cso.habittracker.ui.theme.Surface
import com.cso.habittracker.ui.theme.TextPrimary
import com.cso.habittracker.ui.theme.TextSecondary
import com.cso.habittracker.ui.theme.TextTertiary
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek

@Composable
fun TodayRoot(
    onNavigateToStats: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    viewModel: TodayViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is TodayEvent.NavigateToEdit -> onNavigateToEdit(event.habitId)
            TodayEvent.NavigateToCreate -> onNavigateToCreate()
            TodayEvent.NavigateToStats -> onNavigateToStats()
        }
    }

    TodayScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun TodayScreen(
    state: TodayState,
    onAction: (TodayAction) -> Unit
) {
    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(TodayAction.OnOpenCreate) },
                shape = RoundedCornerShape(16.dp),
                containerColor = Primary,
                contentColor = TextPrimary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add habit")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // Custom top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = state.formattedDate,
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.displayLarge,
                        color = TextPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Surface)
                        .clickable { onAction(TodayAction.OnOpenStats) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.BarChart,
                        contentDescription = "Statistics",
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Content area
            when {
                state.isLoading -> Unit
                state.hasNoHabits -> NoHabitsEmptyState(modifier = Modifier.weight(1f))
                state.isRestDay -> RestDayState(modifier = Modifier.weight(1f))
                else -> {
                    DailyProgressSection(
                        completedCount = state.completedCount,
                        totalCount = state.totalCount
                    )
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 0.dp,
                            end = 20.dp,
                            bottom = innerPadding.calculateBottomPadding() + 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.scheduledHabits, key = { it.habit.id }) { item ->
                            HabitCard(
                                habit = item.habit,
                                isCompleted = item.isCompleted,
                                onCardClick = { onAction(TodayAction.OnOpenEdit(item.habit.id)) },
                                onToggle = { onAction(TodayAction.OnToggleCompletion(item.habit.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyProgressSection(
    completedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Daily progress",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = TextTertiary
            )
            Text(
                text = "$completedCount / $totalCount",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Secondary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(50)),
            color = Primary,
            trackColor = Surface,
            drawStopIndicator = {}
        )
    }
}

@Composable
private fun NoHabitsEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No habits yet",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap + to add your first habit",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = TextTertiary
            )
        }
    }
}

@Composable
private fun RestDayState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Nothing scheduled today",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enjoy your rest day",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = TextTertiary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun TodayScreenPreview() {
    val habit = Habit(
        id = 1L,
        name = "Morning Run",
        icon = HabitIcon.RUN,
        scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
        currentStreak = 12
    )
    HabitTrackerTheme {
        TodayScreen(
            state = TodayState(
                formattedDate = "Monday, April 6",
                scheduledHabits = listOf(
                    HabitWithCompletion(habit, isCompleted = true),
                    HabitWithCompletion(habit.copy(id = 2L, name = "Read 30min", icon = HabitIcon.READ, currentStreak = 5), isCompleted = false)
                ),
                completedCount = 1,
                totalCount = 2,
                isLoading = false
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun TodayScreenEmptyPreview() {
    HabitTrackerTheme {
        TodayScreen(
            state = TodayState(
                formattedDate = "Monday, April 6",
                isLoading = false,
                hasNoHabits = true
            ),
            onAction = {}
        )
    }
}
