package com.unclled.habittracker.ui.add_habit.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unclled.habittracker.features.database.HabitsDatabase
import com.unclled.habittracker.features.database.HabitRepository
import com.unclled.habittracker.features.database.model.ActivityEntity
import com.unclled.habittracker.features.database.model.HabitEntity
import com.unclled.habittracker.features.database.model.ReminderTimeEntity
import com.unclled.habittracker.utils.DateFormatter
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class AddHabitVM(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    private val _buttonStates = mutableStateListOf(false, false, false, false, false, false, false)
    val buttonStates: List<Boolean> get() = _buttonStates
    var selectedItemIndex by mutableIntStateOf(0)
    var habitName by mutableStateOf(TextFieldValue(""))
    var habitDescription by mutableStateOf(TextFieldValue(""))
    var selectedPeriodValue by mutableIntStateOf(2)
    var imageUri by mutableStateOf("")
    var countStates by mutableStateOf("")

    private val utils = DateFormatter()

    init {
        val habitDao = HabitsDatabase.getInstance(application).getHabitDao()
        repository = HabitRepository(habitDao)
    }

    fun toggleButtonState(index: Int) {
        _buttonStates[index] = !_buttonStates[index]
    }

    fun saveToDatabase(remindId: Int, countStates: String) {
        viewModelScope.launch {
            val reminderTimeId = repository.insertReminderTime(
                ReminderTimeEntity(
                    reminderEntityId = 0,
                    remindId = remindId,
                    reminder = countStates
                )
            )
            val nextActivityCheck = utils.getNextNotificationDate(
                utils.formattedDate,
                ReminderTimeEntity(reminderTimeId, remindId, countStates)
            )
            val activityId = repository.insertActivity(
                ActivityEntity(
                    activityEntityId = 0,
                    dateOfCreating = utils.formattedDate,
                    lastActivityCheck = "01-01-2000",
                    nextActivityCheck = nextActivityCheck
                )
            )
            val newHabit = HabitEntity(
                id = 0,
                habitName = habitName.text,
                habitDescription = habitDescription.text,
                imageUri = imageUri,
                reminderId = reminderTimeId,
                activityId = activityId
            )

            repository.insertNewHabit(newHabit)
        }
    }

    fun saveToDatabase(remindId: Int) {
        var reminder = if (remindId == 0) "1" else selectedPeriodValue.toString()
        viewModelScope.launch {
            val reminderTimeId = repository.insertReminderTime(
                ReminderTimeEntity(
                    reminderEntityId = 0,
                    remindId = remindId,
                    reminder = reminder
                )
            )
            val nextActivityCheck = utils.getNextNotificationDate(
                utils.formattedDate,
                ReminderTimeEntity(reminderTimeId, remindId, reminder)
            )
            val activityId = repository.insertActivity(
                ActivityEntity(
                    activityEntityId = 0,
                    dateOfCreating = utils.formattedDate,
                    lastActivityCheck = "01-01-2000",
                    nextActivityCheck = nextActivityCheck
                )
            )
            val newHabit = HabitEntity(
                id = 0,
                habitName = habitName.text,
                habitDescription = habitDescription.text,
                imageUri = imageUri,
                reminderId = reminderTimeId,
                activityId = activityId
            )

            repository.insertNewHabit(newHabit)
        }
    }

    fun saveImageToCache(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val cacheDir = context.cacheDir
            val file = File(cacheDir, "image_${System.currentTimeMillis()}.jpg")
            val outputStream: OutputStream = file.outputStream()

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateHabitData(id: Long, remindId: Int) {
        var reminder =
            if (remindId == 0) "1"
            else if (remindId == 2 || remindId == 4) selectedPeriodValue.toString()
            else countStates
        viewModelScope.launch {
            repository.updateReminderTime(
                ReminderTimeEntity(
                    reminderEntityId = id,
                    remindId = remindId,
                    reminder = reminder
                )
            )
            val nextActivityCheck = utils.getNextNotificationDate(
                utils.formattedDate,
                ReminderTimeEntity(id, remindId, reminder)
            )
            repository.updateActivity(id, nextActivityCheck!!)
            val newHabit = HabitEntity(
                id = id,
                habitName = habitName.text,
                habitDescription = habitDescription.text,
                imageUri = imageUri,
                reminderId = id,
                activityId = id
            )

            repository.updateHabit(newHabit)
        }
    }
}