package com.unclled.habittracker.ui.statistic.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unclled.habittracker.features.database.HabitRepository
import com.unclled.habittracker.features.database.HabitsDatabase
import kotlinx.coroutines.launch

class StatisticVM(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    var daysInRow: List<Int> = listOf()
    var numberOfHabits by mutableIntStateOf(0)

    init {
        val habitDao = HabitsDatabase.getInstance(application).getHabitDao()
        repository = HabitRepository(habitDao)
        getDaysInRow()
        getNumberOfHabits()
    }

    fun getDaysInRow() {
        viewModelScope.launch {
            daysInRow = repository.getDaysInRow()
        }
    }

    fun getNumberOfHabits() {
        viewModelScope.launch {
            numberOfHabits = repository.getNumberOfHabits().toInt()
        }
    }

    fun getMaxFromSP(sharedPreferences: SharedPreferences): Int {
        val max = sharedPreferences.getInt("max_days_in_row", 0)
        val maxInList = daysInRow.toList().maxOrNull() ?: 0
        if (max < maxInList) {
            with(sharedPreferences.edit()) {
                putInt("max_days_in_row", maxInList)
                apply()
            }
            return maxInList
        }
        return max
    }
}