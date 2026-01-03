package com.amrdeveloper.linkhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = lightBlue600,
    onPrimary = sky,

    secondary = teal200,
    onSecondary = white,

    onSurface = black,

    error = red,
    onError = white
)

private val DarkColors = darkColorScheme(
    primary = black,
    onPrimary = sky,

    secondary = teal200,
    onSecondary = black,

    onSurface = whiteSmoke,

    error = red,
    onError = white
)

@Composable
fun LinkhubAppTheme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}