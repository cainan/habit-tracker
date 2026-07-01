package com.cso.habittracker.feature.habit.data

import com.cso.habittracker.core.database.dao.HabitCompletionDao
import com.cso.habittracker.core.database.dao.HabitDao
import com.cso.habittracker.core.database.entity.HabitCompletionEntity
import com.cso.habittracker.feature.habit.data.mapper.toEntity
import com.cso.habittracker.feature.habit.data.mapper.toHabit
import com.cso.habittracker.feature.habit.data.mapper.toScheduledDays
import com.cso.habittracker.feature.habit.domain.datasource.HabitLocalDataSource
import com.cso.habittracker.feature.habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoomHabitDataSource(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao
) : HabitLocalDataSource {

    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAll().map { entities -> entities.map { it.toHabit() } }

    override fun getHabitById(id: Long): Flow<Habit?> =
        habitDao.getById(id).map { it?.toHabit() }

    override suspend fun insertHabit(habit: Habit): Long =
        habitDao.insert(habit.toEntity())

    override suspend fun updateHabit(habit: Habit) =
        habitDao.update(habit.toEntity())

    override suspend fun deleteHabit(habit: Habit) =
        habitDao.delete(habit.toEntity())

    override suspend fun toggleCompletion(habitId: Long, date: LocalDate) {
        val epochDay = date.toEpochDay()
        val alreadyCompleted = completionDao.isCompletedOnce(habitId, epochDay)

        if (alreadyCompleted) {
            completionDao.delete(habitId, epochDay)
        } else {
            completionDao.insert(
                HabitCompletionEntity(
                    habitId = habitId,
                    completedDate = epochDay,
                    createdAt = System.currentTimeMillis()
                )
            )
        }

        recalculateStreaks(habitId)
    }

    override fun getCompletionsInRange(
        from: LocalDate,
        to: LocalDate
    ): Flow<Map<LocalDate, List<Long>>> =
        completionDao.getCompletionsInRange(from.toEpochDay(), to.toEpochDay())
            .map { completions ->
                completions.groupBy(
                    keySelector = { LocalDate.ofEpochDay(it.completedDate) },
                    valueTransform = { it.habitId }
                )
            }

    override fun isCompleted(habitId: Long, date: LocalDate): Flow<Boolean> =
        completionDao.isCompleted(habitId, date.toEpochDay())

    private suspend fun recalculateStreaks(habitId: Long) {
        val entity = habitDao.getByIdOnce(habitId) ?: return

        val completionEpochDays = completionDao.getCompletionDatesForHabit(
            habitId = habitId,
            fromDate = entity.createdAt
        ).toSet()

        val currentStreak = StreakCalculator.calculate(
            scheduledDays = entity.toScheduledDays(),
            completionEpochDays = completionEpochDays,
            createdAtEpochDay = entity.createdAt
        )

        val bestStreak = maxOf(entity.bestStreak, currentStreak)

        habitDao.updateStreaks(
            id = habitId,
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            updatedAt = System.currentTimeMillis()
        )
    }
}
