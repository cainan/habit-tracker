package com.cso.habittracker.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    @ColumnInfo(name = "is_monday") val isMonday: Boolean,
    @ColumnInfo(name = "is_tuesday") val isTuesday: Boolean,
    @ColumnInfo(name = "is_wednesday") val isWednesday: Boolean,
    @ColumnInfo(name = "is_thursday") val isThursday: Boolean,
    @ColumnInfo(name = "is_friday") val isFriday: Boolean,
    @ColumnInfo(name = "is_saturday") val isSaturday: Boolean,
    @ColumnInfo(name = "is_sunday") val isSunday: Boolean,
    @ColumnInfo(name = "current_streak") val currentStreak: Int = 0,
    @ColumnInfo(name = "best_streak") val bestStreak: Int = 0,
    // LocalDate.toEpochDay() — streak baseline; days before this are not "missed"
    @ColumnInfo(name = "created_at") val createdAt: Long,
    // System.currentTimeMillis() epoch millis
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
