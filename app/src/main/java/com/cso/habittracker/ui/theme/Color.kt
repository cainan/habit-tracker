package com.cso.habittracker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// --- Raw palette ---

val Background = Color(0xFF0F0F15)
val Surface = Color(0xFF1A1A2E)
val SurfaceElevated = Color(0xFF252540)
val SurfaceBright = Color(0xFF2E2E4A)

val Primary = Color(0xFF6C63FF)
val PrimaryLight = Color(0xFF8B7BFF)
val PrimaryDark = Color(0xFF4A42CC)
val Secondary = Color(0xFFA855F7)

val Success = Color(0xFF34D399)
val Streak = Color(0xFFF59E0B)
val Accent = Color(0xFFF472B6)
val Destructive = Color(0xFFEF4444)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0B0C0)
val TextTertiary = Color(0xFF666680)
val Border = Color(0xFF333350)

// --- M3 color scheme ---

val HabitTrackerDarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryLight,
    secondary = Secondary,
    onSecondary = TextPrimary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = Border,
    error = Destructive,
    onError = TextPrimary,
)

// --- Custom semantic colors with no M3 slot ---

object AppColors {
    val success = Success
    val streak = Streak
    val accent = Accent
    val textSecondary = TextSecondary
    val textTertiary = TextTertiary
    val border = Border
    val surfaceElevated = SurfaceElevated
    val surfaceBright = SurfaceBright
}
