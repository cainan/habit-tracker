package com.cso.habittracker

import android.app.Application
import com.cso.habittracker.core.database.di.databaseModule
import com.cso.habittracker.feature.habit.data.di.habitDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HabitTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HabitTrackerApp)
            modules(
                databaseModule,
                habitDataModule
            )
        }
    }
}
