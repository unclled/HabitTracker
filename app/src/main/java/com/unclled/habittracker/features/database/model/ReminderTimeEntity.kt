package com.unclled.habittracker.features.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("reminderTime")
data class ReminderTimeEntity(
    @PrimaryKey(autoGenerate = true) val reminderEntityId: Long,
    val remindId: Int, //0 - daily, 1 - weekly, 2 - few week, 3 - monthly, 4 - few months
    val reminder: String
)
