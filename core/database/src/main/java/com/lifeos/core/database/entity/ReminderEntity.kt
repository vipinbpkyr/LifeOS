package com.lifeos.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.core.model.Reminder
import com.lifeos.core.model.ReminderPriority

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val dueTimestamp: Long,
    val isCompleted: Boolean,
    val priority: String,
    val aiSuggestedCategory: String?,
    val createdAt: Long
) {
    fun toDomainModel(): Reminder = Reminder(
        id = id,
        title = title,
        description = description,
        dueTimestamp = dueTimestamp,
        isCompleted = isCompleted,
        priority = runCatching { ReminderPriority.valueOf(priority) }.getOrDefault(ReminderPriority.MEDIUM),
        aiSuggestedCategory = aiSuggestedCategory,
        createdAt = createdAt
    )

    companion object {
        fun fromDomainModel(reminder: Reminder): ReminderEntity = ReminderEntity(
            id = reminder.id,
            title = reminder.title,
            description = reminder.description,
            dueTimestamp = reminder.dueTimestamp,
            isCompleted = reminder.isCompleted,
            priority = reminder.priority.name,
            aiSuggestedCategory = reminder.aiSuggestedCategory,
            createdAt = reminder.createdAt
        )
    }
}
