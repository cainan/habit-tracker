package com.cso.habittracker.feature.habit.presentation.createedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.habittracker.feature.habit.domain.datasource.HabitLocalDataSource
import com.cso.habittracker.feature.habit.domain.model.Habit
import com.cso.habittracker.feature.habit.domain.model.HabitIcon
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek

data class CreateEditState(
    val mode: ScreenMode = ScreenMode.CREATE,
    val habitId: Long = -1L,
    val selectedIcon: HabitIcon = HabitIcon.RUN,
    val name: String = "",
    val selectedDays: Set<DayOfWeek> = setOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    ),
    val iconPickerVisible: Boolean = true,
    val nameError: Boolean = false,
    val daysError: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isSaving: Boolean = false
)

enum class ScreenMode { CREATE, EDIT }

sealed interface CreateEditAction {
    data object NavigateBack : CreateEditAction
    data class SelectIcon(val icon: HabitIcon) : CreateEditAction
    data class ChangeName(val name: String) : CreateEditAction
    data class ToggleDay(val day: DayOfWeek) : CreateEditAction
    data object ToggleIconPicker : CreateEditAction
    data object Save : CreateEditAction
    data object RequestDelete : CreateEditAction
    data object ConfirmDelete : CreateEditAction
    data object DismissDelete : CreateEditAction
}

sealed interface CreateEditEvent {
    data object NavigateBack : CreateEditEvent
}

class CreateEditViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val dataSource: HabitLocalDataSource
) : ViewModel() {

    private val habitId: Long = savedStateHandle["habitId"] ?: -1L
    private val mode: ScreenMode = if (habitId == -1L) ScreenMode.CREATE else ScreenMode.EDIT

    private val _state = MutableStateFlow(
        CreateEditState(
            mode = mode,
            habitId = habitId,
            name = savedStateHandle["name"] ?: "",
            iconPickerVisible = mode == ScreenMode.CREATE
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<CreateEditEvent>()
    val events = _events.receiveAsFlow()

    init {
        if (mode == ScreenMode.EDIT) {
            viewModelScope.launch {
                // Only populate from DB if there is no saved state (first load after process death)
                if (savedStateHandle.get<String>("name") == null) {
                    dataSource.getHabitById(habitId).first()?.let { habit ->
                        _state.update {
                            it.copy(
                                selectedIcon = habit.icon,
                                name = habit.name,
                                selectedDays = habit.scheduledDays
                            )
                        }
                        savedStateHandle["name"] = habit.name
                    }
                }
            }
        }
    }

    fun onAction(action: CreateEditAction) {
        when (action) {
            CreateEditAction.NavigateBack -> viewModelScope.launch {
                _events.send(CreateEditEvent.NavigateBack)
            }
            is CreateEditAction.ChangeName -> {
                savedStateHandle["name"] = action.name
                _state.update { it.copy(name = action.name, nameError = false) }
            }
            is CreateEditAction.SelectIcon -> _state.update { it.copy(selectedIcon = action.icon) }
            is CreateEditAction.ToggleDay -> _state.update { current ->
                val days = if (action.day in current.selectedDays) {
                    current.selectedDays - action.day
                } else {
                    current.selectedDays + action.day
                }
                current.copy(selectedDays = days, daysError = false)
            }
            CreateEditAction.ToggleIconPicker -> _state.update { it.copy(iconPickerVisible = !it.iconPickerVisible) }
            CreateEditAction.Save -> save()
            CreateEditAction.RequestDelete -> _state.update { it.copy(showDeleteDialog = true) }
            CreateEditAction.ConfirmDelete -> delete()
            CreateEditAction.DismissDelete -> _state.update { it.copy(showDeleteDialog = false) }
        }
    }

    private fun save() {
        val current = _state.value
        val nameError = current.name.isBlank()
        val daysError = current.selectedDays.isEmpty()
        if (nameError || daysError) {
            _state.update { it.copy(nameError = nameError, daysError = daysError) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            if (mode == ScreenMode.CREATE) {
                dataSource.insertHabit(
                    Habit(
                        name = current.name.trim(),
                        icon = current.selectedIcon,
                        scheduledDays = current.selectedDays
                    )
                )
            } else {
                val existing = dataSource.getHabitById(habitId).first() ?: return@launch
                dataSource.updateHabit(
                    existing.copy(
                        name = current.name.trim(),
                        icon = current.selectedIcon,
                        scheduledDays = current.selectedDays
                    )
                )
            }
            _events.send(CreateEditEvent.NavigateBack)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            val existing = dataSource.getHabitById(habitId).first() ?: return@launch
            dataSource.deleteHabit(existing)
            _events.send(CreateEditEvent.NavigateBack)
        }
    }
}
