package com.unclled.habittracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val LocalColors = staticCompositionLocalOf { baseLightPalette }

data class ColorPalette(
    val background: Color,
    val primary: Color,
    val text: Color,
    val secondaryText: Color,
    val icon: Color,
    val border: Color,
)

val baseLightPalette = ColorPalette(
    background = primaryLightBackground,
    primary = primaryColor,
    text = primaryTextColor,
    secondaryText = secondaryTextColor,
    icon = iconColor,
    border = borderColor
)
val baseDarkPalette = baseLightPalette.copy(
    background = primaryDarkBackground,
    primary = primaryColor,
    text = primaryTextColor,
    secondaryText = secondaryTextColor,
    icon = iconColor,
    border = borderColor
)

@Composable
fun HabitTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (!darkTheme) baseLightPalette
    else baseDarkPalette

    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = primaryDarkBackground
        )
    } else {
        systemUiController.setStatusBarColor(
            color = primaryLightBackground
        )
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}
