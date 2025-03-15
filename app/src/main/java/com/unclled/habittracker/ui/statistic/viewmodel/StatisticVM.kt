package com.unclled.habittracker.ui.statistic.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.unclled.habittracker.features.database.HabitRepository
import com.unclled.habittracker.features.database.HabitsDatabase
import com.unclled.habittracker.features.database.model.HabitEntity
import kotlinx.coroutines.launch

class StatisticVM(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    var daysInRow: List<Int> = listOf()

    init {
        val habitDao = HabitsDatabase.getInstance(application).getHabitDao()
        repository = HabitRepository(habitDao)
        getDaysInRow()
    }

    private fun getDaysInRow() {
        viewModelScope.launch {
            daysInRow = repository.getDaysInRow()
        }
    }

}