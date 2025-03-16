package com.unclled.habittracker.utils

import com.unclled.habittracker.features.database.model.ReminderTimeEntity
import com.unclled.habittracker.ui.habits.model.DayOfWeek
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.addAll

class DateFormatter {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance()
    val formattedDate: String = dateFormat.format(calendar.time)


    fun convertToDayOfWeek(days: String, reminderId: Int): List<DayOfWeek> {
        val daysOfWeek = mutableListOf<DayOfWeek>()
        when (reminderId) {
            0 -> daysOfWeek.addAll(DayOfWeek.entries.toTypedArray()) //daily
            1 -> for (day in days) { //weekly
                when (day) {
                    '0' -> daysOfWeek.add(DayOfWeek.Monday)
                    '1' -> daysOfWeek.add(DayOfWeek.Tuesday)
                    '2' -> daysOfWeek.add(DayOfWeek.Wednesday)
                    '3' -> daysOfWeek.add(DayOfWeek.Thursday)
                    '4' -> daysOfWeek.add(DayOfWeek.Friday)
                    '5' -> daysOfWeek.add(DayOfWeek.Saturday)
                    '6' -> daysOfWeek.add(DayOfWeek.Sunday)
                }
            }

            else -> return daysOfWeek
        }

        return daysOfWeek
    }

    fun calculateDaysUntilNextDate(nextDateStr: String?): Int {
        if (nextDateStr.isNullOrEmpty()) return 0

        return try {
            val nextDate = dateFormat.parse(nextDateStr) ?: return 0
            val today = Calendar.getInstance().time

            val diffMillis = nextDate.time - today.time
            //Округляем в днях
            (diffMillis / (1000 * 60 * 60 * 24)).toInt()
        } catch (_: ParseException) {
            0
        }
    }

    fun getNextNotificationDate(selectedDays: List<DayOfWeek>, currentDay: DayOfWeek): DayOfWeek? {
        val sortedDays = selectedDays.sortedBy { it.ordinal }
        for (day in sortedDays) {
            if (day.ordinal > currentDay.ordinal) {
                return day
            }
        }
        return sortedDays.firstOrNull()
    }

    fun getNextNotificationDate(dateOfCreating: String, reminder: ReminderTimeEntity): String? {
        val createdDate = try {
            dateFormat.parse(dateOfCreating)
        } catch (_: ParseException) {
            return null
        }

        val calendar = Calendar.getInstance()
        createdDate?.let {
            calendar.time = it
        }

        when (reminder.remindId) {
            0 -> calendar.add(Calendar.DAY_OF_YEAR, 1)
            1 -> {
                val listOfDays = convertToDayOfWeek(reminder.reminder, 1)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2
                val currentDayOfWeek: DayOfWeek = if (dayOfWeek < 0) {
                    DayOfWeek.entries[dayOfWeek + 7]
                } else {
                    DayOfWeek.entries[dayOfWeek]
                }
                calendar.add(
                    Calendar.DAY_OF_YEAR, calculateDaysUntilNextNotification(
                        getNextNotificationDate(
                            listOfDays,
                            currentDayOfWeek
                        ),
                        currentDayOfWeek
                    )
                )
            }

            2 -> calendar.add(Calendar.DAY_OF_YEAR, 7 * reminder.reminder.toInt())
            3 -> calendar.add(Calendar.MONTH, 1)
            4 -> calendar.add(Calendar.MONTH, reminder.reminder.toInt())
            else -> return null
        }

        return dateFormat.format(calendar.time)
    }

    fun calculateDaysUntilNextNotification(nextDay: DayOfWeek?, currentDay: DayOfWeek): Int {
        return if (nextDay!!.ordinal > currentDay.ordinal) {
            nextDay.ordinal - currentDay.ordinal
        } else {
            7 - (currentDay.ordinal - nextDay.ordinal)
        }
    }

    fun dayAddition(day: Int): String {
        if (day % 100 / 10 == 1) {
            return "дней"
        }

        return when (day % 10) {
            1 -> "день"
            2, 3, 4 -> "дня"
            else -> "дней"
        }
    }
}