package com.unclled.habittracker.features.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.unclled.habittracker.features.database.model.ActivityEntity
import com.unclled.habittracker.features.database.model.HabitEntity
import com.unclled.habittracker.features.database.model.HabitWithDetails
import com.unclled.habittracker.features.database.model.ReminderTimeEntity

@Dao
interface HabitDao {

    /* INSERT */
    @Insert(entity = HabitEntity::class)
    suspend fun insertNewHabit(habit: HabitEntity)

    @Insert(entity = ActivityEntity::class)
    suspend fun insertActivity(activity: ActivityEntity): Long

    @Insert(entity = ReminderTimeEntity::class)
    suspend fun insertReminderTime(reminder: ReminderTimeEntity): Long


    /* SELECT */
    @Query("SELECT COUNT(*) FROM habit")
    suspend fun getNumberOfHabits(): Long

    @Query("SELECT daysInRow FROM activity ORDER BY daysInRow DESC LIMIT 1")
    suspend fun getMaxDaysInRow(): Int

    @Query(
        """
        SELECT 
            habit.id,
            habit.habitName,
            habit.habitDescription,
            habit.imageUri,
            habit.reminderId,
            habit.activityId,
            
            activity.activityEntityId,
            activity.daysInRow,
            activity.dateOfCreating,
            activity.lastActivityCheck,
            activity.nextActivityCheck,
            
            reminderTime.reminderEntityId,
            reminderTime.remindId,
            reminderTime.reminder
        FROM habit
        JOIN activity ON habit.activityId = activity.activityEntityId
        JOIN reminderTime ON habit.reminderId = reminderTime.reminderEntityId
    """
    )
    fun getAllHabitsWithDetails(): LiveData<List<HabitWithDetails>>

    @Query("SELECT daysInRow FROM activity")
    suspend fun getDaysInRow(): List<Int>

    @Query("SELECT * FROM habit LIMIT 1")
    suspend fun getAnyHabit(): List<HabitEntity>


    /* UPDATE */
    @Query(
        """
        UPDATE activity SET 
            lastActivityCheck = :lastActivityCheck, 
            nextActivityCheck = :nextActivityCheck
        WHERE activityEntityId = :id
    """
    )
    suspend fun updateDaysInRow(id: Long, lastActivityCheck: String, nextActivityCheck: String?)

    @Query("UPDATE activity SET daysInRow = daysInRow + 1 WHERE activityEntityId = :id")
    suspend fun increaseDayInRow(id: Long)

    @Update
    fun updateReminderTime(reminder: ReminderTimeEntity)

    @Query("UPDATE activity SET nextActivityCheck = :nextActivityCheck WHERE activityEntityId = :id")
    fun updateActivity(id: Long, nextActivityCheck: String)

    @Update
    fun updateHabit(habit: HabitEntity)

    /* DELETE */
    @Transaction
    suspend fun deleteHabitAndRelatedData(id: Long) {
        deleteHabitById(id)
        deleteActivityByHabitId(id)
        deleteReminderByHabitId(id)
    }

    @Query("DELETE FROM habit WHERE id = :id")
    suspend fun deleteHabitById(id: Long)

    @Query("DELETE FROM activity WHERE activityEntityId = :habitId")
    suspend fun deleteActivityByHabitId(habitId: Long)

    @Query("DELETE FROM reminderTime WHERE reminderEntityId = :habitId")
    suspend fun deleteReminderByHabitId(habitId: Long)
}