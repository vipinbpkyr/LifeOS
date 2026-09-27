package com.lifeos.feature.reminders.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lifeos.core.model.ReminderPriority
import com.lifeos.core.navigation.LifeOsDestination
import com.lifeos.feature.reminders.domain.usecase.DeleteReminderUseCase
import com.lifeos.feature.reminders.domain.usecase.GetReminderByIdUseCase
import com.lifeos.feature.reminders.domain.usecase.ToggleReminderUseCase
import com.lifeos.feature.reminders.domain.usecase.UpdateReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReminderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val toggleReminderUseCase: ToggleReminderUseCase
) : ViewModel() {

    private val reminderId: String = savedStateHandle.toRoute<LifeOsDestination.ReminderDetail>().reminderId

    private val _uiState = MutableStateFlow(ReminderDetailUiState())
    val uiState: StateFlow<ReminderDetailUiState> = _uiState.asStateFlow()

    private var initialLoaded = false

    init {
        loadReminder()
    }

    private fun loadReminder() {
        viewModelScope.launch {
            getReminderByIdUseCase(reminderId).collect { reminder ->
                if (reminder == null) {
                    if (initialLoaded) {
                        _uiState.update { it.copy(isSavedOrDeleted = true) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, isNotFound = true) }
                    }
                } else {
                    if (!initialLoaded) {
                        initialLoaded = true
                        _uiState.update {
                            it.copy(
                                reminder = reminder,
                                title = reminder.title,
                                description = reminder.description,
                                priority = reminder.priority,
                                category = reminder.aiSuggestedCategory ?: "Personal",
                                isCompleted = reminder.isCompleted,
                                isLoading = false,
                                isNotFound = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                reminder = reminder,
                                isCompleted = reminder.isCompleted
                            )
                        }
                    }
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDesc: String) {
        _uiState.update { it.copy(description = newDesc) }
    }

    fun onPriorityChange(newPriority: ReminderPriority) {
        _uiState.update { it.copy(priority = newPriority) }
    }

    fun onCategoryChange(newCategory: String) {
        _uiState.update { it.copy(category = newCategory) }
    }

    fun onToggleCompletion() {
        viewModelScope.launch {
            toggleReminderUseCase(reminderId)
        }
    }

    fun saveChanges() {
        val currentState = _uiState.value
        val existingReminder = currentState.reminder ?: return
        val trimmedTitle = currentState.title.trim()
        if (trimmedTitle.isBlank()) return

        val updated = existingReminder.copy(
            title = trimmedTitle,
            description = currentState.description.trim(),
            priority = currentState.priority,
            aiSuggestedCategory = currentState.category.trim().ifBlank { null },
            isCompleted = currentState.isCompleted
        )

        viewModelScope.launch {
            updateReminderUseCase(updated)
            _uiState.update { it.copy(isSavedOrDeleted = true) }
        }
    }

    fun deleteReminder() {
        viewModelScope.launch {
            deleteReminderUseCase(reminderId)
            _uiState.update { it.copy(isSavedOrDeleted = true) }
        }
    }
}
