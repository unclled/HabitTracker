package com.unclled.habittracker.navigation

sealed class NavRoutes(val route: String) {
    data object Habits: NavRoutes("habits")
    data object Statistic: NavRoutes("statistic")
    data object AddHabit: NavRoutes("add_habit")
}