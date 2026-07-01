package com.cso.habittracker.feature.habit.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: HabitIcon,
    val scheduledDays: Set<DayOfWeek>,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val createdAt: LocalDate = LocalDate.now()
)
