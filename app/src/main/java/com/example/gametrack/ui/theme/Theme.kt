package com.example.gametrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkPrimary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onTertiary = DarkBackground,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = DarkText
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightPrimary,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LightText,
    onSurface = LightText,
    onSurfaceVariant = LightText
)

@Composable
fun GameTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}