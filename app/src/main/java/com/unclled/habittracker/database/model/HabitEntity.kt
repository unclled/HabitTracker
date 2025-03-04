package com.unclled.habittracker.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = ReminderTimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["reminderId"]
        )
    ]
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val habitName: String,
    val habitDescription: String,
    val reminder: String,
    val reminderId: Int,
    val imageUri: String?
)
