package com.unclled.habittracker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val LocalColors = staticCompositionLocalOf { baseLightPalette }

data class ColorPalette(
    val background: Color,
    val primary: Color,
    val text: Color,
    val icon: Color,
    val border: Color,
)

val baseLightPalette = ColorPalette(
    background = primaryLightBackground,
    primary = primaryColor,
    text = primaryTextColor,
    icon = iconColor,
    border = borderColor
)
val baseDarkPalette = baseLightPalette.copy(
    background = primaryDarkBackground,
    primary = primaryColor,
    text = primaryTextColor,
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
