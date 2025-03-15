package com.unclled.habittracker.ui.statistic.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.bar.BarGraph
import com.jaikeerthick.composable_graphs.composables.bar.model.BarData
import com.unclled.habittracker.theme.LocalColors
import com.unclled.habittracker.ui.statistic.viewmodel.StatisticVM

@Composable
fun StatisticView(vm: StatisticVM) {
    val colors = LocalColors.current
    val daysInRowList = vm.daysInRow
    var indexes: Int = 1
    if (daysInRowList.isNotEmpty()) {
        var barDataList = mutableListOf<BarData>()
        for (i in 0..daysInRowList.size - 1) {
            barDataList.add(BarData(indexes.toString(), daysInRowList[i]))
            indexes++
        }
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .background(colors.background)
                .padding(top = 40.dp, bottom = 65.dp)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            BarGraph(
                data = barDataList,
            )
        }
    } else {
        Text("No data available")
    }
}