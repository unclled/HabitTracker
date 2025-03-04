package com.unclled.habittracker.ui.habits.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.unclled.habittracker.database.model.HabitEntity
import com.unclled.habittracker.ui.habits.viewmodel.HabitsVM
import com.unclled.habittracker.ui.theme.LocalColors

@Composable
fun HabitsView(vm: HabitsVM) {
    val colors = LocalColors.current
    val habits by vm.habits.observeAsState(emptyList())

    if (habits.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .systemBarsPadding()
                .background(colors.background)
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxSize()
        ) {
            items(habits) { habit ->
                ItemCard(habit)
            }
        }
    } else {
        NothingToShow()
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
            modifier = Modifier.padding(top = 10.dp, start = 14.dp, end = 14.dp)
        )
    }
}

@Composable
fun ItemCard(habit: HabitEntity) {
    val colors = LocalColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, color = colors.border)
            )
            .padding(horizontal = 12.dp)

    ) {
        IconButton(
            onClick = { /* TODO edit habit */ },
            modifier = Modifier.size(24.dp).align(Alignment.End).padding(top = 6.dp, end = 2.dp)
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = null,
                tint = colors.text
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(100.dp))

            Text(
                habit.habitName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )
            Text(
                "дней",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.text,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f).padding(end = 26.dp)
            )


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (habit.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = habit.imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(82.dp)
                        .border(1.dp, colors.border, CircleShape),
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(colors.border)
                        .border(1.dp, colors.border, CircleShape)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Возвращайтесь через\n" + habit.reminder + " дней",
                    fontSize = 14.sp,
                    color = colors.text
                )

                Button(
                    onClick = { /* TODO confirm completion */ },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text(
                        "Подтвердить активность",
                        fontSize = 10.sp,
                        color = colors.background
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .border(1.dp, colors.border, CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "111",
                    fontSize = 28.sp,
                    color = colors.text,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            "подряд",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colors.text,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 17.dp, top = 4.dp)
        )
    }
}

