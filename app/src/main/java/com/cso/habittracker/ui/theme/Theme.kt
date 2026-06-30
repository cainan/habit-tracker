package com.cso.habittracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// TODO: define light palette when light theme is designed
private val HabitTrackerLightColorScheme = lightColorScheme()

@Composable
fun HabitTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) HabitTrackerDarkColorScheme else HabitTrackerLightColorScheme,
        typography = HabitTrackerTypography,
        content = content,
    )
}
