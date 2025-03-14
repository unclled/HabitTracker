package com.unclled.habittracker.features.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unclled.habittracker.features.database.model.HabitEntity

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

    @Query("UPDATE habit SET habitName = :name, habitDescription = :desc, reminder = :reminder, " +
            "reminderId = :remId, imageUri = :imageUri WHERE id = :id")
    suspend fun updateHabitData(id: Long, name: String, desc: String, reminder: String, remId: Int, imageUri: String)

    @Query("SELECT daysInRow FROM habit")
    suspend fun getDaysInRow(): List<Int>

    @Query("SELECT * FROM habit LIMIT 1")
    suspend fun getAnyHabit(): List<HabitEntity>
}