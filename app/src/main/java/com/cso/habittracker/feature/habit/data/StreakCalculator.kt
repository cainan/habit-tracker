package com.cso.habittracker.feature.habit.data

import java.time.DayOfWeek
import java.time.LocalDate

object StreakCalculator {

    /**
     * Counts consecutive completed scheduled days walking backward from today.
     *
     * - Non-scheduled days are skipped (they don't break the streak).
     * - If today is a scheduled day but not yet completed, it is skipped rather than treated
     *   as a break — the streak is still considered "alive" for the current day.
     * - Days before [createdAtEpochDay] are never counted.
     */
    fun calculate(
        scheduledDays: Set<DayOfWeek>,
        completionEpochDays: Set<Long>,
        createdAtEpochDay: Long,
        today: LocalDate = LocalDate.now()
    ): Int {
        val todayEpochDay = today.toEpochDay()
        var streak = 0
        var current = today

        while (true) {
            val epochDay = current.toEpochDay()

            if (epochDay < createdAtEpochDay) break

            val isScheduled = current.dayOfWeek in scheduledDays
            val isCompleted = epochDay in completionEpochDays
            val isToday = epochDay == todayEpochDay

            if (isScheduled) {
                when {
                    isCompleted -> streak++
                    isToday -> Unit // still in progress — don't break
                    else -> break
                }
            }

            current = current.minusDays(1)
        }

        return streak
    }
}
