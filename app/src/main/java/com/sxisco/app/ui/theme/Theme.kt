package com.sxisco.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = SxiscoPurple,
    primaryContainer = SxiscoPurpleLight,
    background = SxiscoBackground,
    surface = SxiscoSurface,
    onBackground = SxiscoTextPrimary,
    onSurface = SxiscoTextPrimary,
)

private val DarkColors = darkColorScheme(
    primary = SxiscoPurple,
    primaryContainer = SxiscoPurpleLight,
)

@Composable
fun SxiscoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = SxiscoTypography,
        content = content
    )
}
