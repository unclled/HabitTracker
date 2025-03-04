package com.unclled.habittracker.ui.add_habit.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unclled.habittracker.database.HabitsDatabase
import com.unclled.habittracker.database.HabitRepository
import com.unclled.habittracker.database.model.HabitEntity
import kotlinx.coroutines.launch

class AddHabitVM(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    private val _buttonStates = mutableStateListOf(false, false, false, false, false, false, false)
    val buttonStates: List<Boolean> get() = _buttonStates
    var selectedItemIndex by mutableIntStateOf(0)
    var habitName by mutableStateOf(TextFieldValue(""))
    var habitDescription by mutableStateOf(TextFieldValue(""))
    var imageUri by mutableStateOf<Uri?>(null)
    var selectedPeriodValue by mutableIntStateOf(2)

    init {
        val habitDao = HabitsDatabase.getInstance(application).getHabitDao()
        repository = HabitRepository(habitDao)
    }

    fun toggleButtonState(index: Int) {
        _buttonStates[index] = !_buttonStates[index]
    }

    fun saveToDatabase(reminderId: Int, countStates: String) {
        val newHabit = HabitEntity(
            id = 0,
            habitName = habitName.text,
            habitDescription = habitDescription.text,
            reminder = countStates,
            reminderId = reminderId,
            imageUri = imageUri.toString()
        )

        viewModelScope.launch {
            repository.insertNewHabit(newHabit)
        }
    }

    fun saveToDatabase(reminderId: Int) {
        var reminder = selectedPeriodValue.toString()
        if (reminderId == 0)
            reminder = "1"
        val newHabit = HabitEntity(
            id = 0,
            habitName = habitName.text,
            habitDescription = habitDescription.text,
            reminder = reminder,
            reminderId = reminderId,
            imageUri = imageUri.toString()
        )

        viewModelScope.launch {
            repository.insertNewHabit(newHabit)
        }
    }
}