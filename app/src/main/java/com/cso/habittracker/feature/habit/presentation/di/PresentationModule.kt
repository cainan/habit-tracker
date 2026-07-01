package com.cso.habittracker.feature.habit.presentation.di

import com.cso.habittracker.feature.habit.presentation.createedit.CreateEditViewModel
import com.cso.habittracker.feature.habit.presentation.stats.StatsViewModel
import com.cso.habittracker.feature.habit.presentation.today.TodayViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::TodayViewModel)
    viewModelOf(::StatsViewModel)
    viewModelOf(::CreateEditViewModel)
}
