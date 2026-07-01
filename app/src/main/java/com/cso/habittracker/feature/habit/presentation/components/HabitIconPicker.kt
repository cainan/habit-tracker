package com.cso.habittracker.feature.habit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import com.cso.habittracker.ui.theme.Border
import com.cso.habittracker.ui.theme.Primary
import com.cso.habittracker.ui.theme.PrimaryLight
import com.cso.habittracker.ui.theme.Secondary
import com.cso.habittracker.ui.theme.SurfaceElevated
import com.cso.habittracker.ui.theme.TextPrimary

private val allIcons = HabitIcon.entries
private const val COLUMNS = 5

@Composable
fun HabitIconPicker(
    selected: HabitIcon,
    onSelect: (HabitIcon) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = allIcons.chunked(COLUMNS)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { rowIcons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowIcons.forEach { icon ->
                    val isSelected = icon == selected
                    val shape = RoundedCornerShape(12.dp)
                    val containerColor = if (isSelected) Primary else SurfaceElevated
                    val iconTint = if (isSelected) TextPrimary else Secondary
                    val borderMod = if (isSelected) {
                        Modifier.border(2.dp, PrimaryLight, shape)
                    } else {
                        Modifier.border(1.dp, Border, shape)
                    }

                    HabitIconBox(
                        icon = icon,
                        containerSize = 56.dp,
                        iconSize = 24.dp,
                        cornerRadius = 12.dp,
                        iconTint = iconTint,
                        containerColor = containerColor,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .then(borderMod)
                            .clip(shape)
                            .clickable { onSelect(icon) }
                    )
                }
                // Fill remaining slots in the last row if it's not full
                repeat(COLUMNS - rowIcons.size) {
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
