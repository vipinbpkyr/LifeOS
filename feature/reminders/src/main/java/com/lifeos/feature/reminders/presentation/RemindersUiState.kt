package com.lifeos.feature.reminders.presentation

import com.lifeos.core.model.Reminder

data class RemindersUiState(
    val reminders: List<Reminder> = emptyList(),
    val isLoading: Boolean = false,
    val filterOnlyPending: Boolean = false,
    val newReminderTitle: String = "",
    val errorMessage: String? = null
)
