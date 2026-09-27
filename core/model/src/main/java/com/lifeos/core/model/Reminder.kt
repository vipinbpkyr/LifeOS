package com.lifeos.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReminderPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

@Serializable
data class Reminder(
    val id: String,
    val title: String,
    val description: String = "",
    val dueTimestamp: Long,
    val isCompleted: Boolean = false,
    val priority: ReminderPriority = ReminderPriority.MEDIUM,
    val aiSuggestedCategory: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
