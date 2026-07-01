package com.cso.habittracker.core.database.di

import androidx.room.Room
import com.cso.habittracker.core.database.HabitDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            HabitDatabase::class.java,
            "habit_tracker.db"
        ).build()
    }
    single { get<HabitDatabase>().habitDao() }
    single { get<HabitDatabase>().habitCompletionDao() }
}
