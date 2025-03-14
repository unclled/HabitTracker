package com.unclled.habittracker.features.database

import androidx.lifecycle.LiveData
import com.unclled.habittracker.features.database.dao.HabitDao
import com.unclled.habittracker.features.database.model.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitRepository(private val habitDao: HabitDao) {
    val habitsList: LiveData<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun insertNewHabit(habitEntity: HabitEntity) {
        withContext(Dispatchers.IO) {
            habitDao.insertNewHabit(habitEntity)
        }
    }

    suspend fun deleteHabitById(id: Long) {
        withContext(Dispatchers.IO) {
            habitDao.deleteHabitById(id)
        }
    }

    suspend fun increaseDayInRow(id: Long) {
        withContext(Dispatchers.IO) {
            habitDao.increaseDayInRow(id)
        }
    }

    suspend fun getAnyHabit(): List<HabitEntity> {
        return withContext(Dispatchers.IO) {
            habitDao.getAnyHabit()
        }
    }
}
