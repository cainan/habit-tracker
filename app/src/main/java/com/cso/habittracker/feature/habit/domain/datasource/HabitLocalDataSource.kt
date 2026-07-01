package com.cso.habittracker.feature.habit.domain.datasource

import com.cso.habittracker.feature.habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitLocalDataSource {
    fun getAllHabits(): Flow<List<Habit>>
    fun getHabitById(id: Long): Flow<Habit?>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun toggleCompletion(habitId: Long, date: LocalDate)
    fun getCompletionsInRange(from: LocalDate, to: LocalDate): Flow<Map<LocalDate, List<Long>>>
    fun isCompleted(habitId: Long, date: LocalDate): Flow<Boolean>
}
