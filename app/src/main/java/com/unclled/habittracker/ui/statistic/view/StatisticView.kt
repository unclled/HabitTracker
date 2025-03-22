package com.unclled.habittracker.ui.statistic.view

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.composables.bar.BarGraph
import com.jaikeerthick.composable_graphs.composables.bar.model.BarData
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphColors
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphFillType
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphStyle
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.unclled.habittracker.theme.ColorPalette
import com.unclled.habittracker.theme.LocalColors
import com.unclled.habittracker.ui.statistic.viewmodel.StatisticVM

@Composable
fun StatisticView(vm: StatisticVM) {
    val context = LocalContext.current
    val colors = LocalColors.current
    val sharedPreferences = context.getSharedPreferences("data", MODE_PRIVATE)

    LaunchedEffect(Unit) {
        vm.getDaysInRow()
        vm.getNumberOfHabits()
    }

    val daysInRowList = vm.daysInRow

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .background(colors.background)
            .padding(top = 40.dp, bottom = 65.dp)
            .padding(horizontal = 14.dp)
            .fillMaxSize()
    ) {
        StatisticPoint("Всего целей", vm.numberOfHabits)
        StatisticPoint("Максимум дней подряд", vm.getMaxFromSP(sharedPreferences))
        if (daysInRowList.isNotEmpty()) {
            ShowGraph(daysInRowList)
        } else {
            NothingToShow()
        }
    }
}


@Composable
fun ShowGraph(daysInRow: List<Int>) {
    val colors = LocalColors.current
    var indexes = 1
    var barDataList = mutableListOf<BarData>()
    for (i in 0..daysInRow.size - 1) {
        barDataList.add(BarData(indexes.toString(), daysInRow[i]))
        indexes++
    }
    val style = BarGraphStyle(
        visibility = BarGraphVisibility(
            isYAxisLabelVisible = true
        ),
        yAxisLabelPosition = LabelPosition.RIGHT,
        colors = BarGraphColors(
            xAxisTextColor = colors.text,
            yAxisTextColor = colors.text,
            fillType = BarGraphFillType.Gradient(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(133, 231, 159),
                        Color(116, 224, 155),
                        Color(150, 239, 163),
                        Color(167, 245, 167),
                        Color(184, 251, 191)
                    )
                )
            )
        )
    )


    Text(
        "Дней подряд у каждой цели",
        fontSize = 19.sp,
        textAlign = TextAlign.Start,
        color = colors.text,
        modifier = Modifier.padding(top = 22.dp, start = 9.dp)
    )
    BarGraph(
        modifier = Modifier.padding(top = 22.dp),
        data = barDataList,
        style = style,
        onBarClick = { }
    )
}

@Composable
fun StatisticPoint(message: String, number: Int) {
    val colors = LocalColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            message,
            fontSize = 19.sp,
            textAlign = TextAlign.Start,
            color = colors.text
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.dp, Color(133, 231, 159), CircleShape)
                .background(colors.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                number.toString(),
                fontSize = 24.sp,
                color = colors.text,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NothingToShow() {
    val colors = LocalColors.current

    Column(
        Modifier
            .systemBarsPadding()
            .background(colors.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Полную статистику собрать не удалось!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colors.text
        )
        Text(
            "график начнет отображаться после добавления первой цели",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colors.secondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
        )
    }
}
