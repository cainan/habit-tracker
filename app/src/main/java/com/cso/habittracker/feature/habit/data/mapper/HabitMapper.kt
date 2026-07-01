package com.cso.habittracker.feature.habit.data.mapper

import com.cso.habittracker.core.database.entity.HabitEntity
import com.cso.habittracker.feature.habit.domain.model.Habit
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import java.time.DayOfWeek
import java.time.LocalDate

fun HabitEntity.toHabit(): Habit = Habit(
    id = id,
    name = name,
    icon = HabitIcon.valueOf(icon),
    scheduledDays = toScheduledDays(),
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    createdAt = LocalDate.ofEpochDay(createdAt)
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    icon = icon.name,
    isMonday = DayOfWeek.MONDAY in scheduledDays,
    isTuesday = DayOfWeek.TUESDAY in scheduledDays,
    isWednesday = DayOfWeek.WEDNESDAY in scheduledDays,
    isThursday = DayOfWeek.THURSDAY in scheduledDays,
    isFriday = DayOfWeek.FRIDAY in scheduledDays,
    isSaturday = DayOfWeek.SATURDAY in scheduledDays,
    isSunday = DayOfWeek.SUNDAY in scheduledDays,
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    createdAt = createdAt.toEpochDay(),
    updatedAt = System.currentTimeMillis()
)

fun HabitEntity.toScheduledDays(): Set<DayOfWeek> = buildSet {
    if (isMonday) add(DayOfWeek.MONDAY)
    if (isTuesday) add(DayOfWeek.TUESDAY)
    if (isWednesday) add(DayOfWeek.WEDNESDAY)
    if (isThursday) add(DayOfWeek.THURSDAY)
    if (isFriday) add(DayOfWeek.FRIDAY)
    if (isSaturday) add(DayOfWeek.SATURDAY)
    if (isSunday) add(DayOfWeek.SUNDAY)
}
