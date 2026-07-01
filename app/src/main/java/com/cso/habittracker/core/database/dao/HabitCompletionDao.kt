package com.cso.habittracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cso.habittracker.core.database.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completion: HabitCompletionEntity)

    @Query("DELETE FROM habit_completions WHERE habit_id = :habitId AND completed_date = :completedDate")
    suspend fun delete(habitId: Long, completedDate: Long)

    @Query("""
        SELECT * FROM habit_completions
        WHERE habit_id = :habitId AND completed_date BETWEEN :fromDate AND :toDate
        ORDER BY completed_date DESC
    """)
    fun getCompletionsForHabit(habitId: Long, fromDate: Long, toDate: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE completed_date BETWEEN :fromDate AND :toDate")
    fun getCompletionsInRange(fromDate: Long, toDate: Long): Flow<List<HabitCompletionEntity>>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM habit_completions
            WHERE habit_id = :habitId AND completed_date = :completedDate
        )
    """)
    fun isCompleted(habitId: Long, completedDate: Long): Flow<Boolean>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM habit_completions
            WHERE habit_id = :habitId AND completed_date = :completedDate
        )
    """)
    suspend fun isCompletedOnce(habitId: Long, completedDate: Long): Boolean

    @Query("""
        SELECT completed_date FROM habit_completions
        WHERE habit_id = :habitId AND completed_date >= :fromDate
        ORDER BY completed_date DESC
    """)
    suspend fun getCompletionDatesForHabit(habitId: Long, fromDate: Long): List<Long>
}
