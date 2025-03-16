package com.unclled.habittracker.features.database

import androidx.lifecycle.LiveData
import com.unclled.habittracker.features.database.dao.HabitDao
import com.unclled.habittracker.features.database.model.ActivityEntity
import com.unclled.habittracker.features.database.model.HabitEntity
import com.unclled.habittracker.features.database.model.HabitWithDetails
import com.unclled.habittracker.features.database.model.ReminderTimeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitRepository(private val habitDao: HabitDao) {
    //val habitsList: LiveData<List<HabitEntity>> = habitDao.getAllHabits()
    val habitsList: LiveData<List<HabitWithDetails>> = habitDao.getAllHabitsWithDetails()

    suspend fun insertNewHabit(habitEntity: HabitEntity) {
        habitDao.insertNewHabit(habitEntity)
    }

    suspend fun insertReminderTime(reminderTime: ReminderTimeEntity): Long {
        return habitDao.insertReminderTime(reminderTime)
    }

    suspend fun insertActivity(activity: ActivityEntity): Long {
        return habitDao.insertActivity(activity)
    }

    suspend fun deleteHabitById(id: Long) {
        habitDao.deleteHabitAndRelatedData(id)
    }

    suspend fun increaseDayInRow(id: Long) {
        habitDao.increaseDayInRow(id)
    }

    suspend fun getAnyHabit(): List<HabitEntity> {
        return withContext(Dispatchers.IO) {
            habitDao.getAnyHabit()
        }
    }

    suspend fun getDaysInRow(): List<Int> {
        return withContext(Dispatchers.IO) {
            habitDao.getDaysInRow()
        }
    }

    suspend fun getNumberOfHabits(): Long {
        return withContext(Dispatchers.IO) {
            habitDao.getNumberOfHabits()
        }
    }

    suspend fun updateCheckInformation(
        id: Long,
        lastActivityCheck: String,
        nextActivityCheck: String?
    ) {
        withContext(Dispatchers.IO) {
            habitDao.updateDaysInRow(id, lastActivityCheck, nextActivityCheck)
        }
    }

    suspend fun updateReminderTime(reminder: ReminderTimeEntity) {
        withContext(Dispatchers.IO) {
            habitDao.updateReminderTime(reminder)
        }
    }

    suspend fun updateActivity(id: Long, nextActivityCheck: String) {
        withContext(Dispatchers.IO) {
            habitDao.updateActivity(id, nextActivityCheck)
        }
    }

    suspend fun updateHabit(habit: HabitEntity) {
        withContext(Dispatchers.IO) {
            habitDao.updateHabit(habit)
        }
    }
}
