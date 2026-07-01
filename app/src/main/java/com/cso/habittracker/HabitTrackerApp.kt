package com.cso.habittracker

import android.app.Application
// import android.util.Log
import com.cso.habittracker.core.database.di.databaseModule
// import com.cso.habittracker.feature.habit.data.DatabaseSeeder
import com.cso.habittracker.feature.habit.data.di.habitDataModule
// import com.cso.habittracker.feature.habit.domain.datasource.HabitLocalDataSource
import com.cso.habittracker.feature.habit.presentation.di.presentationModule
// import kotlinx.coroutines.DelicateCoroutinesApi
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.GlobalScope
// import kotlinx.coroutines.launch
// import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HabitTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HabitTrackerApp)
            modules(
                databaseModule,
                habitDataModule,
                presentationModule
            )
        }
        // Uncomment to seed the DB with sample data on first launch (clear app data first):
        // val dataSource: HabitLocalDataSource = get()
        // @OptIn(DelicateCoroutinesApi::class)
        // GlobalScope.launch(Dispatchers.IO) {
        //     try {
        //         DatabaseSeeder(dataSource).seed()
        //     } catch (e: Exception) {
        //         Log.e("DatabaseSeeder", "Seeding failed", e)
        //     }
        // }
    }
}
