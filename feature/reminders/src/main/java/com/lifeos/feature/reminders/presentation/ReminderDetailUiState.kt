package com.lifeos.feature.reminders.presentation

import com.lifeos.core.model.Reminder
import com.lifeos.core.model.ReminderPriority

data class ReminderDetailUiState(
    val reminder: Reminder? = null,
    val title: String = "",
    val description: String = "",
    val priority: ReminderPriority = ReminderPriority.MEDIUM,
    val category: String = "Personal",
    val isCompleted: Boolean = false,
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val isSavedOrDeleted: Boolean = false
)
