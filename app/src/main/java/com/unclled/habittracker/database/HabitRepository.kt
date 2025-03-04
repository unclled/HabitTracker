package com.unclled.habittracker.database

import androidx.lifecycle.LiveData
import com.unclled.habittracker.database.dao.HabitDao
import com.unclled.habittracker.database.model.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitRepository(private val habitDao: HabitDao) {
    val habitsList: LiveData<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun insertNewHabit(habitEntity: HabitEntity) {
        withContext(Dispatchers.IO) {
            habitDao.insertNewHabit(habitEntity)
        }
    }

    suspend fun removeHabitById(id: Long) {
        withContext(Dispatchers.IO) {
            habitDao.deleteHabitById(id)
        }
    }
}
