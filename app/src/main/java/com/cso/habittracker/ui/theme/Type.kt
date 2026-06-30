package com.cso.habittracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.cso.habittracker.R

val InterFamily = FontFamily(
    Font(R.font.inter_variable, weight = FontWeight.Medium),
    Font(R.font.inter_variable, weight = FontWeight.SemiBold),
    Font(R.font.inter_variable, weight = FontWeight.Bold),
)

val ManropeFamily = FontFamily(
    Font(R.font.manrope_variable, weight = FontWeight.ExtraBold),
)

val HabitTrackerTypography = Typography(
    // Display / Stat Number — Manrope ExtraBold 26sp
    displayLarge = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
    ),
    // Heading — Manrope ExtraBold 22sp
    headlineLarge = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
    ),
    // Title — Inter SemiBold 16sp
    titleLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    // Body — Inter Medium 14sp
    bodyMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    // Button — Inter Bold 16sp
    labelLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    // Caption — Inter Medium 11sp, 1px letter spacing (applied uppercase at call site)
    labelSmall = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.08.em,
    ),
)
