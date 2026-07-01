package com.cso.habittracker.feature.habit.presentation.today

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Stable
data class TodayState(
    val formattedDate: String = "",
    val scheduledHabits: List<HabitWithCompletion> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true,
    val isRestDay: Boolean = false,
    val hasNoHabits: Boolean = false
)

data class HabitWithCompletion(
    val habit: Habit,
    val isCompleted: Boolean
)

sealed interface TodayAction {
    data class OnToggleCompletion(val habitId: Long) : TodayAction
    data class OnOpenEdit(val habitId: Long) : TodayAction
    data object OnOpenCreate : TodayAction
    data object OnOpenStats : TodayAction
}

sealed interface TodayEvent {
    data class NavigateToEdit(val habitId: Long) : TodayEvent
    data object NavigateToCreate : TodayEvent
    data object NavigateToStats : TodayEvent
}

class TodayViewModel(
    private val dataSource: HabitLocalDataSource
) : ViewModel() {

    private val today = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())

    private val _state = MutableStateFlow(TodayState())
    val state = _state.asStateFlow()

    private val _events = Channel<TodayEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                dataSource.getAllHabits(),
                dataSource.getCompletionsInRange(today, today)
            ) { habits, completionsMap ->
                val completedIds = completionsMap[today] ?: emptyList()
                val dayOfWeek = today.dayOfWeek
                val scheduledHabits = habits.filter { dayOfWeek in it.scheduledDays }
                val isRestDay = habits.isNotEmpty() && scheduledHabits.isEmpty()
                val habitsWithCompletion = scheduledHabits.map { habit ->
                    HabitWithCompletion(habit, habit.id in completedIds)
                }
                TodayState(
                    formattedDate = today.format(dateFormatter),
                    scheduledHabits = habitsWithCompletion,
                    completedCount = habitsWithCompletion.count { it.isCompleted },
                    totalCount = habitsWithCompletion.size,
                    isLoading = false,
                    isRestDay = isRestDay,
                    hasNoHabits = habits.isEmpty()
                )
            }.collect { _state.value = it }
        }
    }

    fun onAction(action: TodayAction) {
        when (action) {
            is TodayAction.OnToggleCompletion -> viewModelScope.launch {
                dataSource.toggleCompletion(action.habitId, today)
            }
            is TodayAction.OnOpenEdit -> viewModelScope.launch {
                _events.send(TodayEvent.NavigateToEdit(action.habitId))
            }
            TodayAction.OnOpenCreate -> viewModelScope.launch {
                _events.send(TodayEvent.NavigateToCreate)
            }
            TodayAction.OnOpenStats -> viewModelScope.launch {
                _events.send(TodayEvent.NavigateToStats)
            }
        }
    }
}
