package com.unclled.habittracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unclled.habittracker.features.database.HabitsDatabase
import com.unclled.habittracker.features.notifcations.NotificationAlarm
import com.unclled.habittracker.features.notifcations.NotificationChannelFactory
import com.unclled.habittracker.navigation.BottomNavBar
import com.unclled.habittracker.navigation.NavRoutes
import com.unclled.habittracker.navigation.UpperBar
import com.unclled.habittracker.theme.HabitTrackerTheme
import com.unclled.habittracker.ui.add_habit.view.AddHabitView
import com.unclled.habittracker.ui.habits.view.HabitsView
import com.unclled.habittracker.ui.habits.viewmodel.HabitsVM
import com.unclled.habittracker.ui.statistic.view.StatisticView
import com.unclled.habittracker.ui.statistic.viewmodel.StatisticVM

class MainActivity : ComponentActivity() {

    private val notificationChannelFactory = NotificationChannelFactory()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    "Разрешите уведомления, чтобы не забывать о своих целях!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HabitsDatabase.getInstance(this)
        notificationChannelFactory.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val notificationAlarm = NotificationAlarm(this)
        notificationAlarm.setDailyNotificationAlarm()

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
    val statisticVM: StatisticVM = viewModel()
    var navNum by rememberSaveable { mutableIntStateOf(0) }
    var mode by rememberSaveable { mutableIntStateOf(0) }
    var habitId by rememberSaveable { mutableLongStateOf(-1) }
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController, startDestination = NavRoutes.Habits.route) {
            composable(NavRoutes.Habits.route) {
                HabitsView(
                    vm = habitsVM,
                    mode = mode,
                    navController = navController,
                    onHabitEdit = { newNavNum -> navNum = newNavNum},
                    onPassId = { passedId -> habitId = passedId}
                )
            }
            composable(NavRoutes.AddHabit.route) {
                AddHabitView(
                    navController = navController,
                    onHabitSaved = { newNavNum -> navNum = newNavNum },
                    mode = mode,
                    habitId = habitId
                )
            }
            composable(NavRoutes.Statistic.route) { StatisticView(statisticVM) }
        }
        UpperBar(
            navNum = navNum,
            mode = mode,
            onModeChanged = { newMode ->
                mode = newMode
            },
            Modifier.align(Alignment.TopCenter)
        )
        BottomNavBar(
            navController = navController,
            navNum = navNum,
            onNavItemSelected = { newNavNum ->
                navNum = newNavNum
            },
            Modifier.align(Alignment.BottomCenter)
        )
    }
}
