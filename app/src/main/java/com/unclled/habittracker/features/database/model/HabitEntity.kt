package com.unclled.habittracker.features.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit",
    foreignKeys = [
        ForeignKey(
            entity = ReminderTimeEntity::class,
            parentColumns = ["reminderEntityId"],
            childColumns = ["reminderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["activityEntityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val habitName: String,
    val habitDescription: String,
    val imageUri: String?,
    val reminderId: Long,
    val activityId: Long
)
