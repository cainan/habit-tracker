package com.cso.habittracker.feature.habit.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class StreakCalculatorTest {

    // All tests pin today to a known Monday so results are fully deterministic.
    // 2024-01-15 = Monday  (verified: 2024-01-01 is also Monday)
    private val today = LocalDate.of(2024, 1, 15)

    // Convenience epoch-day for a creation date far in the past — used whenever
    // the creation boundary should not affect the result.
    private val createdFarPast = LocalDate.of(2023, 1, 1).toEpochDay()

    // -------------------------------------------------------------------------
    // Group 1 — Zero / empty inputs
    // -------------------------------------------------------------------------

    @Test
    fun `returns 0 when scheduledDays is empty`() {
        val result = StreakCalculator.calculate(
            scheduledDays = emptySet(),
            completionEpochDays = emptySet(),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(0, result)
    }

    @Test
    fun `returns 0 when there are no completions and today is not scheduled`() {
        // today = Monday, schedule = WEDNESDAY only
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.WEDNESDAY),
            completionEpochDays = emptySet(),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(0, result)
    }

    @Test
    fun `returns 0 when today is scheduled but not completed and there are no past completions`() {
        // today (Mon) gets the grace skip; the next scheduled Mon in the past breaks immediately
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = emptySet(),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(0, result)
    }

    // -------------------------------------------------------------------------
    // Group 2 — Today's behaviour
    // -------------------------------------------------------------------------

    @Test
    fun `returns 1 when today is scheduled and completed`() {
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = setOf(today.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(1, result)
    }

    @Test
    fun `returns 0 when today is scheduled not completed and no past completions exist`() {
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = emptySet(),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(0, result)
    }

    @Test
    fun `returns 1 when today is not scheduled and the last scheduled day is completed`() {
        // today = Monday (not scheduled); Friday Jan 12 is the last scheduled day
        val lastFriday = LocalDate.of(2024, 1, 12) // Friday
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.FRIDAY),
            completionEpochDays = setOf(lastFriday.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(1, result)
    }

    @Test
    fun `returns 1 when today is scheduled not completed but the previous scheduled day is completed`() {
        // today = Monday (not completed); Sunday Jan 14 is scheduled and completed
        val sunday = LocalDate.of(2024, 1, 14) // Sunday
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY),
            completionEpochDays = setOf(sunday.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(1, result)
    }

    // -------------------------------------------------------------------------
    // Group 3 — Non-scheduled gaps do not break the streak
    // -------------------------------------------------------------------------

    @Test
    fun `non-scheduled days between completions do not break the streak`() {
        // Mon/Wed/Fri schedule; all three days in the current week completed
        // Tue, Thu, Sat, Sun are silently skipped
        val friday = LocalDate.of(2024, 1, 12)
        val wednesday = LocalDate.of(2024, 1, 10)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            completionEpochDays = setOf(
                today.toEpochDay(),
                friday.toEpochDay(),
                wednesday.toEpochDay()
            ),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(3, result)
    }

    @Test
    fun `weekly habit maintains streak across six non-scheduled days between each occurrence`() {
        // MONDAY-only; three consecutive Mondays completed
        val lastMonday = LocalDate.of(2024, 1, 8)
        val twoMondaysAgo = LocalDate.of(2024, 1, 1)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = setOf(
                today.toEpochDay(),
                lastMonday.toEpochDay(),
                twoMondaysAgo.toEpochDay()
            ),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(3, result)
    }

    // -------------------------------------------------------------------------
    // Group 4 — Streak breaks on a missed scheduled day
    // -------------------------------------------------------------------------

    @Test
    fun `streak breaks when a past scheduled day was not completed`() {
        // Mon/Wed/Fri; today (Mon) completed; Fri Jan 12 missed → streak ends at Mon
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            completionEpochDays = setOf(today.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(1, result)
    }

    @Test
    fun `earlier completions are not counted once a missed day breaks the streak`() {
        // Mon/Wed; today (Mon) completed; Wed Jan 10 missed; Mon Jan 8 completed but unreachable
        val lastMonday = LocalDate.of(2024, 1, 8)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            completionEpochDays = setOf(today.toEpochDay(), lastMonday.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(1, result)
    }

    // -------------------------------------------------------------------------
    // Group 5 — Creation date boundary
    // -------------------------------------------------------------------------

    @Test
    fun `days before creation date are not counted even if completed`() {
        // Created on Jan 8 (Mon); Jan 1 completion exists but must be ignored
        val createdOnJan8 = LocalDate.of(2024, 1, 8).toEpochDay()
        val lastMonday = LocalDate.of(2024, 1, 8)
        val twoMondaysAgo = LocalDate.of(2024, 1, 1)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = setOf(
                today.toEpochDay(),
                lastMonday.toEpochDay(),
                twoMondaysAgo.toEpochDay()
            ),
            createdAtEpochDay = createdOnJan8,
            today = today
        )
        assertEquals(2, result)
    }

    @Test
    fun `returns 0 when creation date is in the future`() {
        val tomorrow = today.plusDays(1)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = setOf(today.toEpochDay()),
            createdAtEpochDay = tomorrow.toEpochDay(),
            today = today
        )
        assertEquals(0, result)
    }

    @Test
    fun `returns 1 when created today and today is scheduled and completed`() {
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = setOf(today.toEpochDay()),
            createdAtEpochDay = today.toEpochDay(),
            today = today
        )
        assertEquals(1, result)
    }

    @Test
    fun `returns 0 when created today and today is scheduled but not completed`() {
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = emptySet(),
            createdAtEpochDay = today.toEpochDay(),
            today = today
        )
        assertEquals(0, result)
    }

    // -------------------------------------------------------------------------
    // Group 6 — Completion on a non-scheduled day is ignored
    // -------------------------------------------------------------------------

    @Test
    fun `completion recorded on a non-scheduled day does not contribute to streak`() {
        // MONDAY only scheduled; completion exists on Sunday Jan 14
        val sunday = LocalDate.of(2024, 1, 14)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY),
            completionEpochDays = setOf(sunday.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(0, result)
    }

    // -------------------------------------------------------------------------
    // Group 7 — Date arithmetic edge cases
    // -------------------------------------------------------------------------

    @Test
    fun `streak spanning a year boundary is counted correctly`() {
        // TUESDAY schedule; today = Jan 2 2024 (Tue); completed Jan 2, Dec 26 2023, Dec 19 2023
        val jan2 = LocalDate.of(2024, 1, 2)
        val dec26 = LocalDate.of(2023, 12, 26)
        val dec19 = LocalDate.of(2023, 12, 19)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.TUESDAY),
            completionEpochDays = setOf(jan2.toEpochDay(), dec26.toEpochDay(), dec19.toEpochDay()),
            createdAtEpochDay = LocalDate.of(2023, 1, 1).toEpochDay(),
            today = jan2
        )
        assertEquals(3, result)
    }

    @Test
    fun `streak spanning a leap day is counted correctly`() {
        // Mon/Thu schedule; today = Mar 4 2024 (Mon)
        // Completed: Mar 4 (Mon), Feb 29 (Thu), Feb 26 (Mon), Feb 22 (Thu)
        val mar4 = LocalDate.of(2024, 3, 4)
        val feb29 = LocalDate.of(2024, 2, 29)
        val feb26 = LocalDate.of(2024, 2, 26)
        val feb22 = LocalDate.of(2024, 2, 22)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            completionEpochDays = setOf(
                mar4.toEpochDay(),
                feb29.toEpochDay(),
                feb26.toEpochDay(),
                feb22.toEpochDay()
            ),
            createdAtEpochDay = LocalDate.of(2024, 1, 1).toEpochDay(),
            today = mar4
        )
        assertEquals(4, result)
    }

    // -------------------------------------------------------------------------
    // Group 8 — Combined / longer streaks
    // -------------------------------------------------------------------------

    @Test
    fun `daily habit with 7 consecutive completions returns 7`() {
        val completions = (0L..6L).map { today.minusDays(it).toEpochDay() }.toSet()
        val result = StreakCalculator.calculate(
            scheduledDays = DayOfWeek.entries.toSet(),
            completionEpochDays = completions,
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(7, result)
    }

    @Test
    fun `today incomplete still surfaces the full prior streak`() {
        // Mon/Fri schedule; today (Mon) not completed; Fri Jan 12 and Mon Jan 8 completed
        val friday = LocalDate.of(2024, 1, 12)
        val lastMonday = LocalDate.of(2024, 1, 8)
        val result = StreakCalculator.calculate(
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
            completionEpochDays = setOf(friday.toEpochDay(), lastMonday.toEpochDay()),
            createdAtEpochDay = createdFarPast,
            today = today
        )
        assertEquals(2, result)
    }
}
