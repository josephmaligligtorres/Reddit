package com.joseph.myapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primary = Color(0xFFFF4500)
private val secondary = Color(0xFFC6C6C6)
private val error = Color(0xFFD00036)

val DarkColorPalette = darkColors(
    primary = primary,
    secondary = secondary,
    error = error
)

val LightColorPalette = lightColors(
    primary = primary,
    secondary = secondary,
    error = error
)

@Composable
fun MyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}