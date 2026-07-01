package com.cso.habittracker.ui.components

import androidx.annotation.DrawableRes
import com.cso.habittracker.R
import com.cso.habittracker.feature.habit.domain.model.HabitIcon

@DrawableRes
fun HabitIcon.drawableRes(): Int = when (this) {
    HabitIcon.RUN      -> R.drawable.ic_habit_run
    HabitIcon.READ     -> R.drawable.ic_habit_read
    HabitIcon.WATER    -> R.drawable.ic_habit_water
    HabitIcon.MEDITATE -> R.drawable.ic_habit_meditate
    HabitIcon.SLEEP    -> R.drawable.ic_habit_sleep
    HabitIcon.CODE     -> R.drawable.ic_habit_code
    HabitIcon.MUSIC    -> R.drawable.ic_habit_music
    HabitIcon.COOK     -> R.drawable.ic_habit_cook
    HabitIcon.JOURNAL  -> R.drawable.ic_habit_journal
    HabitIcon.GYM      -> R.drawable.ic_habit_gym
    HabitIcon.YOGA     -> R.drawable.ic_habit_yoga
    HabitIcon.WALK     -> R.drawable.ic_habit_walk
    HabitIcon.CYCLE    -> R.drawable.ic_habit_cycle
    HabitIcon.STUDY    -> R.drawable.ic_habit_study
    HabitIcon.NO_PHONE -> R.drawable.ic_habit_no_phone
    HabitIcon.VITAMINS -> R.drawable.ic_habit_vitamins
    HabitIcon.LANGUAGE -> R.drawable.ic_habit_language
    HabitIcon.GRATITUDE -> R.drawable.ic_habit_gratitude
    HabitIcon.HEALTH   -> R.drawable.ic_habit_health
    HabitIcon.ORGANIZE -> R.drawable.ic_habit_organize
}
