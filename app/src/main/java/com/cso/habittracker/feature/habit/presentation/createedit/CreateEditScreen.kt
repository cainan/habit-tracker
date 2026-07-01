package com.cso.habittracker.feature.habit.presentation.createedit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import com.cso.habittracker.feature.habit.presentation.components.DayOfWeekPicker
import com.cso.habittracker.feature.habit.presentation.components.HabitIconBox
import com.cso.habittracker.feature.habit.presentation.components.HabitIconPicker
import com.cso.habittracker.ui.components.AppButton
import com.cso.habittracker.ui.components.AppDestructiveTextButton
import com.cso.habittracker.ui.components.AppTextField
import com.cso.habittracker.ui.components.AppTopBar
import com.cso.habittracker.ui.components.ObserveAsEvents
import com.cso.habittracker.ui.components.SectionTitle
import com.cso.habittracker.ui.theme.Background
import com.cso.habittracker.ui.theme.Destructive
import com.cso.habittracker.ui.theme.HabitTrackerTheme
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.TextPrimary
import com.cso.habittracker.ui.theme.TextTertiary
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek

@Composable
fun CreateEditRoot(
    habitId: Long,
    onNavigateBack: () -> Unit,
    viewModel: CreateEditViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateEditEvent.NavigateBack -> onNavigateBack()
        }
    }

    CreateEditScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun CreateEditScreen(
    state: CreateEditState,
    onAction: (CreateEditAction) -> Unit
) {
    Scaffold(containerColor = Background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AppTopBar(
                title = if (state.mode == ScreenMode.CREATE) "New Habit" else "Edit Habit",
                onBack = { onAction(CreateEditAction.NavigateBack) }
            )

            // Icon preview / picker toggle
            Column {
                SectionTitle(text = "Icon")
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (state.mode == ScreenMode.EDIT) {
                                Modifier.clickable { onAction(CreateEditAction.ToggleIconPicker) }
                            } else Modifier
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HabitIconBox(
                        icon = state.selectedIcon,
                        containerSize = 72.dp,
                        iconSize = 28.dp,
                        cornerRadius = 20.dp
                    )
                    if (state.mode == ScreenMode.EDIT) {
                        Column {
                            Text(
                                text = state.selectedIcon.name
                                    .replace('_', ' ')
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                fontFamily = InterFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = if (state.iconPickerVisible) "Tap to hide picker" else "Tap to change icon",
                                fontFamily = InterFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedVisibility(
                    visible = state.iconPickerVisible,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    HabitIconPicker(
                        selected = state.selectedIcon,
                        onSelect = { onAction(CreateEditAction.SelectIcon(it)) }
                    )
                }
            }

            // Name field
            AppTextField(
                value = state.name,
                onValueChange = { onAction(CreateEditAction.ChangeName(it)) },
                label = "Habit Name",
                isError = state.nameError
            )
            if (state.nameError) {
                Text(
                    text = "Please enter a habit name",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Destructive,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Days picker
            Column {
                SectionTitle(text = "Schedule")
                Spacer(modifier = Modifier.height(10.dp))
                DayOfWeekPicker(
                    selectedDays = state.selectedDays,
                    onToggle = { onAction(CreateEditAction.ToggleDay(it)) },
                    hasError = state.daysError
                )
                if (state.daysError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Please select at least one day",
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Destructive
                    )
                }
            }

            // Action buttons
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                AppButton(
                    text = if (state.mode == ScreenMode.CREATE) "Save Habit" else "Save Changes",
                    onClick = { onAction(CreateEditAction.Save) },
                    enabled = !state.isSaving
                )
                AppDestructiveTextButton(
                    text = if (state.mode == ScreenMode.CREATE) "Discard" else "Delete Habit",
                    onClick = {
                        if (state.mode == ScreenMode.CREATE) {
                            onAction(CreateEditAction.NavigateBack)
                        } else {
                            onAction(CreateEditAction.RequestDelete)
                        }
                    }
                )
            }
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onAction(CreateEditAction.DismissDelete) },
            title = {
                Text(
                    text = "Delete Habit",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "This will permanently delete this habit and all its completion history. This cannot be undone.",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
            },
            confirmButton = {
                TextButton(onClick = { onAction(CreateEditAction.ConfirmDelete) }) {
                    Text(
                        text = "Delete",
                        color = Destructive,
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(CreateEditAction.DismissDelete) }) {
                    Text(
                        text = "Cancel",
                        color = TextPrimary,
                        fontFamily = InterFamily
                    )
                }
            },
            containerColor = Background
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun CreateScreenPreview() {
    HabitTrackerTheme {
        CreateEditScreen(
            state = CreateEditState(
                mode = ScreenMode.CREATE,
                selectedIcon = HabitIcon.RUN,
                iconPickerVisible = true,
                selectedDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun EditScreenPreview() {
    HabitTrackerTheme {
        CreateEditScreen(
            state = CreateEditState(
                mode = ScreenMode.EDIT,
                selectedIcon = HabitIcon.READ,
                name = "Read 30min",
                iconPickerVisible = false,
                selectedDays = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
            ),
            onAction = {}
        )
    }
}
