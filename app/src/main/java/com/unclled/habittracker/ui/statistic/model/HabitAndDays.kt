package com.unclled.habittracker.ui.statistic.model

/* Возвращать имена + дней подряд вместо индексов */
data class HabitAndDays(
    val habitName: String,
    val daysInRow: Int
)
