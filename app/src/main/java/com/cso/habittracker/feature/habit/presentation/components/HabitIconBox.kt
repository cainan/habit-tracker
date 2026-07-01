package com.cso.habittracker.feature.habit.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import com.cso.habittracker.ui.components.drawableRes
import com.cso.habittracker.ui.theme.AppColors
import com.cso.habittracker.ui.theme.Secondary
import com.cso.habittracker.ui.theme.SurfaceElevated

@Composable
fun HabitIconBox(
    icon: HabitIcon,
    containerSize: Dp,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    iconTint: Color = Secondary,
    containerColor: Color = SurfaceElevated
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(RoundedCornerShape(cornerRadius))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon.drawableRes()),
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconTint),
            modifier = Modifier.size(iconSize)
        )
    }
}
