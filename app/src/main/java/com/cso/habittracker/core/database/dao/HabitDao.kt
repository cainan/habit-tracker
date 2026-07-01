package com.cso.habittracker.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cso.habittracker.core.database.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(habit: HabitEntity): Long

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAll(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getById(id: Long): Flow<HabitEntity?>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getByIdOnce(id: Long): HabitEntity?

    @Query("""
        UPDATE habits
        SET current_streak = :currentStreak, best_streak = :bestStreak, updated_at = :updatedAt
        WHERE id = :id
    """)
    suspend fun updateStreaks(id: Long, currentStreak: Int, bestStreak: Int, updatedAt: Long)
}
