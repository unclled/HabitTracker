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
import com.unclled.habittracker.features.database.model.HabitEntity
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHabitVM(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    private val _buttonStates = mutableStateListOf(false, false, false, false, false, false, false)
    val buttonStates: List<Boolean> get() = _buttonStates
    var selectedItemIndex by mutableIntStateOf(0)
    var habitName by mutableStateOf(TextFieldValue(""))
    var habitDescription by mutableStateOf(TextFieldValue(""))
    var selectedPeriodValue by mutableIntStateOf(2)
    var imageUri by mutableStateOf("")
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val formattedDate = dateFormat.format(calendar.time)

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
            imageUri = imageUri,
            dateOfCreating = formattedDate,
            lastActivityCheck = "" /* TODO check if confirmation day skipped */
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
            imageUri = imageUri,
            dateOfCreating = formattedDate,
            lastActivityCheck = "" /* TODO */
        )

        viewModelScope.launch {
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
}