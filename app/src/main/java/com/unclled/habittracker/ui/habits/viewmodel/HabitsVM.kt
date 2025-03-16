package com.unclled.habittracker.ui.habits.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.unclled.habittracker.features.database.HabitRepository
import com.unclled.habittracker.features.database.HabitsDatabase
import com.unclled.habittracker.features.database.model.HabitEntity
import com.unclled.habittracker.features.database.model.HabitWithDetails
import com.unclled.habittracker.features.database.model.ReminderTimeEntity
import com.unclled.habittracker.ui.habits.model.DayOfWeek
import com.unclled.habittracker.utils.DateFormatter
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HabitsVM(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    private val utils = DateFormatter()
    val habits: LiveData<List<HabitWithDetails>>

    init {
        val habitDao = HabitsDatabase.getInstance(application).getHabitDao()
        repository = HabitRepository(habitDao)
        habits = repository.habitsList
    }

    fun increaseDayInRow(id: Long) {
        viewModelScope.launch {
            repository.increaseDayInRow(id)
        }
    }

    fun deleteHabitById(id: Long) {
        viewModelScope.launch {
            repository.deleteHabitById(id)
        }
    }

    fun nameLikeInSentences(habitName: String): String {
        var result = ""
        for (i in habitName.indices) {
            result += if (i == 0) {
                habitName[i].uppercase()
            } else {
                habitName[i].lowercase()
            }
        }
        return result
    }

    fun updateNextActivityCheck(id: Long, lastActivityCheck: String, reminder: ReminderTimeEntity) {
        viewModelScope.launch {
            repository.updateCheckInformation(
                id,
                lastActivityCheck,
                utils.getNextNotificationDate(
                    utils.formattedDate,
                    reminder
                )
            )
        }
    }
}