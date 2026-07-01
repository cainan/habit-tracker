package com.cso.habittracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cso.habittracker.core.database.dao.HabitCompletionDao
import com.cso.habittracker.core.database.dao.HabitDao
import com.cso.habittracker.core.database.entity.HabitCompletionEntity
import com.cso.habittracker.core.database.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}
