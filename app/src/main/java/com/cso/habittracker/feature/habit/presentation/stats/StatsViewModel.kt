package com.cso.habittracker.feature.habit.presentation.stats

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.habittracker.feature.habit.domain.datasource.HabitLocalDataSource
import com.cso.habittracker.feature.habit.domain.model.Habit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Stable
data class StatsState(
    val thisWeekPct: Int = 0,
    val bestStreak: Int = 0,
    val activeCount: Int = 0,
    val heatmapData: Map<LocalDate, Float> = emptyMap(),
    val streakRows: List<HabitStreakRow> = emptyList(),
    val heatmapStartDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true
)

data class HabitStreakRow(
    val habit: Habit,
    val currentStreak: Int,
    val bestStreak: Int
)

sealed interface StatsAction {
    data object NavigateBack : StatsAction
}

sealed interface StatsEvent {
    data object NavigateBack : StatsEvent
}

class StatsViewModel(private val dataSource: HabitLocalDataSource) : ViewModel() {

    private val _state = MutableStateFlow(StatsState())
    val state = _state.asStateFlow()

    private val _events = Channel<StatsEvent>()
    val events = _events.receiveAsFlow()

    init {
        val today = LocalDate.now()
        val mondayThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val heatmapStart = mondayThisWeek.minusWeeks(3)
        val heatmapEnd = mondayThisWeek.plusDays(6)

        viewModelScope.launch {
            combine(
                dataSource.getAllHabits(),
                dataSource.getCompletionsInRange(mondayThisWeek, today),
                dataSource.getCompletionsInRange(heatmapStart, heatmapEnd)
            ) { habits, weekCompletions, heatmapCompletions ->
                buildState(habits, weekCompletions, heatmapCompletions, mondayThisWeek, heatmapStart, heatmapEnd, today)
            }.collect { _state.value = it }
        }
    }

    private fun buildState(
        habits: List<Habit>,
        weekCompletions: Map<LocalDate, List<Long>>,
        heatmapCompletions: Map<LocalDate, List<Long>>,
        mondayThisWeek: LocalDate,
        heatmapStart: LocalDate,
        heatmapEnd: LocalDate,
        today: LocalDate
    ): StatsState {
        // "This Week %" — days Mon to today
        var scheduledOccurrences = 0
        var completedOccurrences = 0
        var d = mondayThisWeek
        while (!d.isAfter(today)) {
            val habitsOnDay = habits.filter { d.dayOfWeek in it.scheduledDays }
            scheduledOccurrences += habitsOnDay.size
            val doneIds = weekCompletions[d] ?: emptyList()
            completedOccurrences += habitsOnDay.count { it.id in doneIds }
            d = d.plusDays(1)
        }
        val thisWeekPct = if (scheduledOccurrences > 0) completedOccurrences * 100 / scheduledOccurrences else 0

        // Heatmap ratios for each day in the 4-week window
        val heatmapData = mutableMapOf<LocalDate, Float>()
        var hd = heatmapStart
        while (!hd.isAfter(heatmapEnd)) {
            if (!hd.isAfter(today)) {
                val habitsOnDay = habits.filter { hd.dayOfWeek in it.scheduledDays }
                heatmapData[hd] = if (habitsOnDay.isEmpty()) {
                    0f
                } else {
                    val doneIds = heatmapCompletions[hd] ?: emptyList()
                    habitsOnDay.count { it.id in doneIds }.toFloat() / habitsOnDay.size
                }
            }
            hd = hd.plusDays(1)
        }

        val streakRows = habits
            .sortedByDescending { it.currentStreak }
            .map { HabitStreakRow(it, it.currentStreak, it.bestStreak) }

        return StatsState(
            thisWeekPct = thisWeekPct,
            bestStreak = habits.maxOfOrNull { it.bestStreak } ?: 0,
            activeCount = habits.size,
            heatmapData = heatmapData,
            streakRows = streakRows,
            heatmapStartDate = heatmapStart,
            today = today,
            isLoading = false
        )
    }

    fun onAction(action: StatsAction) {
        when (action) {
            StatsAction.NavigateBack -> viewModelScope.launch {
                _events.send(StatsEvent.NavigateBack)
            }
        }
    }
}
