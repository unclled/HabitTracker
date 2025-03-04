package com.unclled.habittracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("reminderTime")
data class ReminderTimeEntity(
    @PrimaryKey val id: Int,
    val remindEvery: String,
    val daysBetween: Int,
)
