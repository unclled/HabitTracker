package com.unclled.habittracker.features.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("activity")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val activityEntityId: Long,
    val daysInRow: Int = 0,
    val dateOfCreating: String,
    val lastActivityCheck: String?,
    val nextActivityCheck: String?
)
