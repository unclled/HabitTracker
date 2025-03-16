package com.unclled.habittracker.features.database.model

import androidx.room.Embedded

data class HabitWithDetails(
    @Embedded val habit: HabitEntity,
    @Embedded val activityInfo: ActivityEntity,
    @Embedded val reminderTime: ReminderTimeEntity
)
/*
    @Embedded сообщает Room, что поля habit, activityInfo и reminderTime
    являются вложенными объектами, и Room должен маппить их из результата запроса
*/