package com.unclled.habittracker.ui.habits.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.unclled.habittracker.R
import com.unclled.habittracker.features.database.model.ActivityEntity
import com.unclled.habittracker.features.database.model.HabitEntity
import com.unclled.habittracker.features.database.model.ReminderTimeEntity
import com.unclled.habittracker.navigation.NavRoutes
import com.unclled.habittracker.theme.LocalColors
import com.unclled.habittracker.ui.habits.viewmodel.HabitsVM
import com.unclled.habittracker.utils.DateFormatter
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

@Composable
fun HabitsView(
    vm: HabitsVM,
    mode: Int,
    navController: NavController,
    onHabitEdit: (Int) -> Unit,
    onPassId: (Long) -> Unit
) {
    val colors = LocalColors.current
    val habits by vm.habits.observeAsState(emptyList())

    if (habits.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .systemBarsPadding()
                .background(colors.background)
                .padding(top = 40.dp, bottom = 65.dp)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            items(habits) { habit ->
                ItemCard(
                    habit.habit,
                    habit.activityInfo,
                    habit.reminderTime,
                    vm,
                    mode,
                    navController,
                    onHabitEdit,
                    onPassId
                )
                Spacer(Modifier.padding(vertical = 8.dp))
            }
        }
    } else {
        NothingToShow()
    }
}

@Composable
fun ItemCard(
    habit: HabitEntity,
    activity: ActivityEntity,
    reminder: ReminderTimeEntity,
    vm: HabitsVM,
    mode: Int,
    navController: NavController,
    onHabitEdit: (Int) -> Unit,
    onPassId: (Long) -> Unit
) {
    val alpha = 0.2f
    val enabled = false
    val colors = LocalColors.current
    when (mode) {
        0 -> { //обычное представление
            InnerItems(vm, habit, activity, reminder)
        }

        1 -> { //редактирование цели
            Box(
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        onHabitEdit(2)
                        navController.navigate(NavRoutes.AddHabit.route)
                        onPassId(habit.id)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background)
                        .size(81.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = colors.text
                    )
                }
                InnerItems(vm, habit, activity, reminder, alpha, enabled)
            }
        }

        2 -> { //удаление цели
            Box(
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { vm.deleteHabitById(habit.id) },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background)
                        .size(81.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = colors.text
                    )
                }
                InnerItems(vm, habit, activity, reminder, alpha, enabled)
            }
        }
    }

}

@Composable
fun InnerItems(
    vm: HabitsVM,
    habit: HabitEntity,
    activity: ActivityEntity,
    reminder: ReminderTimeEntity,
    alpha: Float = 1f,
    enabled: Boolean = true
) {
    val colors = LocalColors.current
    val nameChangedCase = vm.nameLikeInSentences(habit.habitName)
    val utils = DateFormatter()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val calendar = Calendar.getInstance()

    val currentDate = LocalDate.parse(dateFormat.format(calendar.time), dateFormatter)
    val nextActivityDate = LocalDate.parse(activity.nextActivityCheck, dateFormatter)
    val comeBackIn =
        if (activity.dateOfCreating == dateFormat.format(calendar.time) && activity.daysInRow == 0) {
            0
        } else {
            ChronoUnit.DAYS.between(currentDate, nextActivityDate).toInt()
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, color = colors.border)
            )
            .padding(horizontal = 12.dp)
            .alpha(alpha)

    ) {
        Spacer(Modifier.padding(top = 10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
        ) {
            Text(
                nameChangedCase,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.text,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp)
                    .width(120.dp)
            )
            Text(
                "дней подряд",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.text,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 2.dp, top = 4.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (habit.imageUri == "") {
                        R.drawable.kitten
                    } else {
                        File(habit.imageUri!!)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(82.dp)
                    .border(1.dp, colors.border, CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFECDA),
                                Color(0xFFFFD8A8)
                            )
                        )
                    ),
                contentScale = ContentScale.FillBounds,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    if (comeBackIn > 0) {
                        "Возвращайтесь через\n" + comeBackIn + " " + utils.dayAddition(comeBackIn)
                    } else {
                        "Пора подтвердить активность!"
                    },
                    fontSize = 16.sp,
                    color = colors.text,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        vm.increaseDayInRow(habit.id)
                        vm.updateNextActivityCheck(
                            habit.id,
                            dateFormat.format(calendar.time),
                            reminder
                        )
                    },
                    enabled = (comeBackIn <= 0 && enabled),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(36.dp)
                        .width(240.dp)
                        .alpha(if (comeBackIn <= 0) 1f else 0.5f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        disabledContainerColor = colors.primary.copy(alpha = 0.5f)
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    Text(
                        "Подтвердить активность",
                        fontSize = 12.sp,
                        color = colors.background,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .border(1.dp, colors.border, CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFECDA),
                                Color(0xFFFFD8A8)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    activity.daysInRow.toString(),
                    fontSize = 28.sp,
                    color = colors.icon,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.padding(6.dp))
    }
}

@Composable
fun NothingToShow() {
    val colors = LocalColors.current

    Column(
        Modifier
            .systemBarsPadding()
            .background(colors.background)
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Вы не установили еще ни одной цели!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colors.text
        )
        Text(
            "нажмите на + внизу экрана, чтобы добавить новую цель",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colors.secondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp, start = 14.dp, end = 14.dp)
        )
    }
}

