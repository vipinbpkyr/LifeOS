package com.lifeos.feature.reminders.data.ai

import com.lifeos.core.ai.AiTool
import com.lifeos.core.model.Reminder
import com.lifeos.core.model.ReminderPriority
import com.lifeos.feature.reminders.domain.repository.ReminderRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class CreateReminderArgs(
    val title: String,
    val priority: String = "HIGH"
)

class CreateReminderAiTool @Inject constructor(
    private val repository: ReminderRepository
) : AiTool {

    override val name: String = "create_reminder"
    override val description: String = "Schedules a new reminder with title and priority."

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun execute(argumentsJson: String): String {
        return try {
            val args = json.decodeFromString<CreateReminderArgs>(argumentsJson)
            val reminder = Reminder(
                id = UUID.randomUUID().toString(),
                title = args.title,
                dueTimestamp = System.currentTimeMillis() + 86400000L, // Tomorrow
                priority = runCatching { ReminderPriority.valueOf(args.priority) }.getOrDefault(ReminderPriority.HIGH),
                aiSuggestedCategory = "AI Scheduled"
            )
            repository.saveReminder(reminder)
            "Successfully created reminder: '${args.title}' with priority ${reminder.priority}"
        } catch (e: Exception) {
            "Failed to create reminder: ${e.message}"
        }
    }
}
