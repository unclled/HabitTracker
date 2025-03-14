package com.unclled.habittracker.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unclled.habittracker.theme.LocalColors

@Composable
fun UpperBar(navNum: Int, mode: Int, onModeChanged: (Int) -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    val headerText = when (navNum) {
        0 -> "Цели"
        -1 -> "Добавить цель"
        1 -> "Статистика"
        else -> ""
    }
    Box(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(32.dp)
            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp))
            .background(colors.statusBar),
        contentAlignment = Alignment.TopCenter
    ) {

        Text(
            headerText,
            fontSize = 22.sp,
            color = colors.text,
            textAlign = TextAlign.Center,
        )
        if (navNum == 0) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { if (mode == 1) onModeChanged(0) else onModeChanged(1) },
                    modifier = Modifier
                        .size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        tint = colors.text
                    )
                }
                IconButton(
                    onClick = { if (mode == 2) onModeChanged(0) else onModeChanged(2) },
                    modifier = Modifier
                        .size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        tint = colors.text
                    )
                }
            }
        }
    }
}