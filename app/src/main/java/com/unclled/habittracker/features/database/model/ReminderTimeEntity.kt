package com.unclled.habittracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("reminderTime")
data class ReminderTimeEntity(
    @PrimaryKey val id: Int, //0 - daily, 1 - weekly, 2 - few week, 3 - monthly, 4 - few months
    val remindEvery: String,
    val daysBetween: Int,
)
