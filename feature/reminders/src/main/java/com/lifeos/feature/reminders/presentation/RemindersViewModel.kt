package com.lifeos.feature.reminders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.core.model.Reminder
import com.lifeos.core.model.ReminderPriority
import com.lifeos.feature.reminders.domain.usecase.DeleteReminderUseCase
import com.lifeos.feature.reminders.domain.usecase.GetRemindersUseCase
import com.lifeos.feature.reminders.domain.usecase.SaveReminderUseCase
import com.lifeos.feature.reminders.domain.usecase.ToggleReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RemindersViewModel @Inject constructor(
    getRemindersUseCase: GetRemindersUseCase,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val toggleReminderUseCase: ToggleReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel() {

    private val filterOnlyPending = MutableStateFlow(false)
    private val newReminderTitle = MutableStateFlow("")

    val uiState: StateFlow<RemindersUiState> = combine(
        getRemindersUseCase(),
        filterOnlyPending,
        newReminderTitle
    ) { reminders, onlyPending, title ->
        val filtered = if (onlyPending) reminders.filter { !it.isCompleted } else reminders
        RemindersUiState(
            reminders = filtered,
            filterOnlyPending = onlyPending,
            newReminderTitle = title,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RemindersUiState(isLoading = true)
    )

    fun onTitleChanged(title: String) {
        newReminderTitle.value = title
    }

    fun onToggleFilter(onlyPending: Boolean) {
        filterOnlyPending.value = onlyPending
    }

    fun addReminder(
        title: String = newReminderTitle.value,
        priority: ReminderPriority = ReminderPriority.MEDIUM,
        category: String = "Personal"
    ) {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isBlank()) return

        viewModelScope.launch {
            val reminder = Reminder(
                id = UUID.randomUUID().toString(),
                title = trimmedTitle,
                dueTimestamp = System.currentTimeMillis() + 86400000L,
                priority = priority,
                aiSuggestedCategory = category
            )
            saveReminderUseCase(reminder)
            if (trimmedTitle == newReminderTitle.value.trim()) {
                newReminderTitle.value = ""
            }
        }
    }

    fun toggleCompletion(id: String) {
        viewModelScope.launch {
            toggleReminderUseCase(id)
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch {
            deleteReminderUseCase(id)
        }
    }
}
