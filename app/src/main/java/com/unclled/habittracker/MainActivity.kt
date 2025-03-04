package com.unclled.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unclled.habittracker.navigation.BottomNavBar
import com.unclled.habittracker.navigation.NavRoutes
import com.unclled.habittracker.ui.add_habit.view.AddHabitView
import com.unclled.habittracker.ui.habits.view.HabitsView
import com.unclled.habittracker.ui.habits.viewmodel.HabitsVM
import com.unclled.habittracker.ui.statistic.view.StatisticView
import com.unclled.habittracker.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()
    val habitsVM: HabitsVM = viewModel()
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController, startDestination = NavRoutes.Habits.route) {
            composable(NavRoutes.Habits.route) { HabitsView(vm = habitsVM) }
            composable(NavRoutes.AddHabit.route) { AddHabitView() }
            composable(NavRoutes.Statistic.route) { StatisticView() }
        }
        BottomNavBar(navController = navController, Modifier.align(Alignment.BottomCenter))
    }
}
