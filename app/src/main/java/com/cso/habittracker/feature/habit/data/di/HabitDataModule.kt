package com.cso.habittracker.feature.habit.data.di

import com.cso.habittracker.feature.habit.data.RoomHabitDataSource
import com.cso.habittracker.feature.habit.domain.datasource.HabitLocalDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val habitDataModule = module {
    singleOf(::RoomHabitDataSource) { bind<HabitLocalDataSource>() }
}
