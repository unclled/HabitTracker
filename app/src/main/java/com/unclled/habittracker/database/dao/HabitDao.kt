package com.unclled.habittracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.unclled.habittracker.database.model.HabitEntity

@Dao
interface HabitDao {
    @Insert(entity = HabitEntity::class)
    suspend fun insertNewHabit(habit: HabitEntity)

    @Query("SELECT * FROM habit")
    fun getAllHabits(): LiveData<List<HabitEntity>>

    @Query("DELETE FROM habit WHERE id = :id")
    suspend fun deleteHabitById(id: Long)

    @Query("UPDATE habit SET daysInRow = daysInRow + 1 WHERE id = :id")
    suspend fun increaseDayInRow(id: Long)
}