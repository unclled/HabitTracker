package com.unclled.habittracker.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unclled.habittracker.R
import com.unclled.habittracker.ui.theme.LocalColors

@Composable
fun NavBar(
    navController: NavController,
    navNum: Int,
    onNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColors.current

    Row(
        modifier
            .fillMaxWidth()
            .background(Color(0xff222223))
            .padding(vertical = 12.dp, horizontal = 46.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onNavItemSelected(0) //передаём в коллбэк новое значение
                navController.navigate(NavRoutes.Habits.route)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.habits),
                contentDescription = "habits",
                tint = if (navNum == 0) colors.primary else colors.text,
                modifier = Modifier.size(36.dp)
            )
        }

        IconButton(
            onClick = {
                onNavItemSelected(1)
                navController.navigate(NavRoutes.Statistic.route)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.statistic),
                contentDescription = "statistics",
                tint = if (navNum == 1) colors.primary else colors.text,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    var navNum by rememberSaveable { mutableIntStateOf(0) }
    Box(
        modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavBar(
            navController,
            navNum = navNum,
            onNavItemSelected = { newNavNum ->
                navNum = newNavNum
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        IconButton(
            onClick = {
                navNum = -1 //сбрасываем состояние, чтобы обе иконки были НЕ выделены
                navController.navigate(NavRoutes.AddHabit.route)
            },
            modifier = Modifier
                .padding(bottom = 30.dp)
                .clip(CircleShape)
                .background(colors.primary)
                .align(Alignment.BottomCenter)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add habit",
                tint = Color(0xff222223),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}