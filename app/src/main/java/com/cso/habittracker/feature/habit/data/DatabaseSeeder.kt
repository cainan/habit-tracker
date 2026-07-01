package com.cso.habittracker.feature.habit.data

import com.cso.habittracker.feature.habit.domain.datasource.HabitLocalDataSource
import com.cso.habittracker.feature.habit.domain.model.Habit
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalDate
import kotlin.random.Random

class DatabaseSeeder(private val dataSource: HabitLocalDataSource) {

    private data class SeedHabit(
        val name: String,
        val icon: HabitIcon,
        val days: Set<DayOfWeek>,
        val completionRate: Float
    )

    private val seeds = listOf(
        SeedHabit("Morning Run",     HabitIcon.RUN,       setOf(MONDAY, WEDNESDAY, FRIDAY),                      0.88f),
        SeedHabit("Read 30min",      HabitIcon.READ,      setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),   0.92f),
        SeedHabit("Drink 2L Water",  HabitIcon.WATER,     DayOfWeek.entries.toSet(),                             0.96f),
        SeedHabit("Meditate",        HabitIcon.MEDITATE,  DayOfWeek.entries.toSet(),                             0.74f),
        SeedHabit("Sleep by 10pm",   HabitIcon.SLEEP,     setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),   0.58f),
        SeedHabit("LeetCode",        HabitIcon.CODE,      setOf(MONDAY, WEDNESDAY, FRIDAY, SUNDAY),              0.78f),
        SeedHabit("Play Guitar",     HabitIcon.MUSIC,     setOf(TUESDAY, THURSDAY, SATURDAY),                    0.50f),
        SeedHabit("Meal Prep",       HabitIcon.COOK,      setOf(MONDAY, WEDNESDAY, SUNDAY),                      0.42f),
        SeedHabit("Journaling",      HabitIcon.JOURNAL,   DayOfWeek.entries.toSet(),                             0.83f),
        SeedHabit("Go to Gym",       HabitIcon.GYM,       setOf(MONDAY, TUESDAY, THURSDAY, FRIDAY),              0.67f),
        SeedHabit("Yoga",            HabitIcon.YOGA,      setOf(TUESDAY, THURSDAY, SATURDAY, SUNDAY),            0.55f),
        SeedHabit("Evening Walk",    HabitIcon.WALK,      DayOfWeek.entries.toSet(),                             0.72f),
        SeedHabit("Vitamins",        HabitIcon.VITAMINS,  DayOfWeek.entries.toSet(),                             0.89f),
        SeedHabit("Gratitude Log",   HabitIcon.GRATITUDE, setOf(MONDAY, WEDNESDAY, FRIDAY, SATURDAY, SUNDAY),    0.65f),
        SeedHabit("No Phone 9pm+",   HabitIcon.NO_PHONE,  DayOfWeek.entries.toSet(),                             0.48f),
    )

    suspend fun seed() {
        if (dataSource.getAllHabits().first().isNotEmpty()) return

        val random = Random(seed = 2026)
        val today = LocalDate.now()
        val createdAt = today.minusDays(35)

        for (seed in seeds) {
            val habitId = dataSource.insertHabit(
                Habit(
                    name = seed.name,
                    icon = seed.icon,
                    scheduledDays = seed.days,
                    createdAt = createdAt
                )
            )
            // Insert completions oldest-first so the final streak recalculation is accurate
            for (daysAgo in 34 downTo 0) {
                val date = today.minusDays(daysAgo.toLong())
                if (date.dayOfWeek in seed.days && random.nextFloat() < seed.completionRate) {
                    dataSource.toggleCompletion(habitId, date)
                }
            }
        }
    }
}
