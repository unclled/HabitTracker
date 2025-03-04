package com.unclled.habittracker.ui.habits.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.unclled.habittracker.database.HabitRepository
import com.unclled.habittracker.database.HabitsDatabase
import com.unclled.habittracker.database.model.HabitEntity

class HabitsVM(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    val habits: LiveData<List<HabitEntity>>

    init {
        val habitDao = HabitsDatabase.getInstance(application).getHabitDao()
        repository = HabitRepository(habitDao)
        habits = repository.habitsList
    }
}