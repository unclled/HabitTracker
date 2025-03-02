package com.unclled.habittracker.ui.add_habit.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class AddHabitVM : ViewModel() {
    private val _buttonStates = mutableStateListOf(false, false, false, false, false, false, false)
    val buttonStates: List<Boolean> get() = _buttonStates
    var selectedItemIndex by mutableIntStateOf(0)
    var habitName by mutableStateOf(TextFieldValue(""))
    var habitDescription by mutableStateOf(TextFieldValue(""))
    var imageUri by mutableStateOf<Uri?>(null)

    fun toggleButtonState(index: Int) {
        _buttonStates[index] = !_buttonStates[index]
    }
}