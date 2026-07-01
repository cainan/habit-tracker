package com.cso.habittracker.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["habit_id"]),
        Index(value = ["habit_id", "completed_date"], unique = true)
    ]
)
data class HabitCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "habit_id") val habitId: Long,
    // LocalDate.toEpochDay()
    @ColumnInfo(name = "completed_date") val completedDate: Long,
    // System.currentTimeMillis() epoch millis
    @ColumnInfo(name = "created_at") val createdAt: Long
)
